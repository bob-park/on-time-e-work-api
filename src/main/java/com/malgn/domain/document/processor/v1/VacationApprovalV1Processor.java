package com.malgn.domain.document.processor.v1;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoField;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.checkerframework.checker.units.qual.A;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.NotSupportException;
import com.malgn.common.exception.ServiceRuntimeException;
import com.malgn.common.model.Id;
import com.malgn.domain.approval.entity.ApprovalLine;
import com.malgn.domain.attendance.feign.AttendanceScheduleFeignClient;
import com.malgn.domain.attendance.model.AddAttendanceScheduleRequest;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.entity.type.VacationSubType;
import com.malgn.domain.document.entity.type.VacationType;
import com.malgn.domain.document.processor.ApprovalProcessor;
import com.malgn.domain.document.repository.DocumentApprovalHistoryRepository;
import com.malgn.domain.document.repository.VacationDocumentRepository;
import com.malgn.domain.user.entity.UserLeaveEntry;
import com.malgn.domain.user.exception.OverLeaveEntryException;
import com.malgn.domain.user.repository.UserCompLeaveEntryRepository;
import com.malgn.domain.user.repository.UserLeaveEntryRepository;

@Slf4j
@RequiredArgsConstructor
public class VacationApprovalV1Processor implements ApprovalProcessor {

    private static final List<Integer> DEFAULT_FAMILY_DAYS_WEEKS = List.of(1, 3);

    private final AttendanceScheduleFeignClient attendanceScheduleClient;

    private final DocumentApprovalHistoryRepository approvalHistoryRepository;
    private final VacationDocumentRepository documentRepository;
    private final UserLeaveEntryRepository userLeaveEntryRepository;
    private final UserCompLeaveEntryRepository userCompLeaveEntryRepository;

    @Override
    public boolean isSupport(DocumentType documentType) {
        return documentType == DocumentType.VACATION;
    }

    @Override
    public DocumentApprovalHistory approve(Id<DocumentApprovalHistory, Long> id) {
        DocumentApprovalHistory history =
            approvalHistoryRepository.getHistory(id)
                .orElseThrow(() -> new NotFoundException(id));

        Document document = history.getDocument();

        ApprovalLine nextLine = history.getApprovalLine().getNext();

        if (nextLine != null) {

            // 다음 결재라인 이 있는 경우 생성
            DocumentApprovalHistory createdHistory =
                DocumentApprovalHistory.builder()
                    .build();

            document.addApprovalHistory(createdHistory);
            nextLine.addApprovalHistory(createdHistory);

            createdHistory = approvalHistoryRepository.save(createdHistory);

            log.debug("created next approval history. ({})", createdHistory);

        } else {

            Id<VacationDocument, Long> documentId = Id.of(VacationDocument.class, document.getId());

            VacationDocument vacationDocument =
                documentRepository.getDocumentById(documentId)
                    .orElseThrow(() -> new NotFoundException(documentId));

            VacationType vacationType = vacationDocument.getVacationType();

            UserLeaveEntry leaveEntry =
                userLeaveEntryRepository.getLeaveEntry(
                        vacationDocument.getUserUniqueId(),
                        vacationDocument.getStartDate().getYear())
                    .orElseThrow(() -> new NotFoundException("No exist leave entry..."));

            try {
                switch (vacationType) {
                    case GENERAL -> leaveEntry.useLeaveDays(vacationDocument.getUsedDays());
                    case COMPENSATORY -> leaveEntry.useCompLeaveDays(vacationDocument.getUsedDays());
                    case OFFICIAL -> {
                        // ignore
                    }

                    default -> throw new NotSupportException();
                }
            } catch (OverLeaveEntryException e) {
                document.reject();
                history.reject("사용할 수 있는 휴가일수를 넘겼습니다.");
                throw new ServiceRuntimeException(e);
            } catch (Exception e) {
                document.reject();
                history.reject("시스템에 문제가 있습니다. 관리자에게 문의해주세요.");
                throw new ServiceRuntimeException(e);
            }

            try {
                addAttendanceSchedule(vacationDocument);
            } catch (Exception e) {
                log.error("failed add attendance schedule. - {}", e.getMessage(), e);
            }

            // 최종 승인자인 경우 최종 승인 처리
            document.approve();

            // 최종 승인인 경우 휴가 감소 처리
            log.debug("final approval document... ({})", document);
        }

        history.approve();

        log.debug("approved history. ({})", history);

        return history;
    }

    @Override
    public DocumentApprovalHistory reject(Id<DocumentApprovalHistory, Long> id, String reason) {
        DocumentApprovalHistory history =
            approvalHistoryRepository.getHistory(id)
                .orElseThrow(() -> new NotFoundException(id));

        Document document = history.getDocument();

        history.reject(reason);
        document.reject();

        log.debug("rejected history. ({})", history);

        return history;
    }

    private void addAttendanceSchedule(VacationDocument vacation) {

        Period between = Period.between(vacation.getStartDate(), vacation.getEndDate());

        long days = between.getDays();

        for (long i = 0; i < days; i++) {

            boolean isSkip = false;
            String dayOffType = AttendanceScheduleFeignClient.DAY_OFF;
            LocalDate date = vacation.getStartDate().plusDays(i);

            switch (date.getDayOfWeek()) {
                case FRIDAY -> {
                    int weekCountOfMonth = date.get(ChronoField.ALIGNED_WEEK_OF_MONTH);

                    if (DEFAULT_FAMILY_DAYS_WEEKS.contains(weekCountOfMonth)) {
                        dayOffType = AttendanceScheduleFeignClient.AM_DAY_OFF;
                    } else {
                        dayOffType = getDayOffType(vacation.getVacationSubType());
                    }
                }

                case SATURDAY, SUNDAY -> {
                    // ignore
                    isSkip = true;
                }

                default -> dayOffType = getDayOffType(vacation.getVacationSubType());
            }

            if (isSkip) {
                continue;
            }

            attendanceScheduleClient.addSchedule(
                AddAttendanceScheduleRequest.builder()
                    .userUniqueId(vacation.getUserUniqueId())
                    .workingDate(date)
                    .dayOffType(dayOffType)
                    .build());
        }

    }

    private String getDayOffType(VacationSubType subType) {

        if (subType == null) {
            return AttendanceScheduleFeignClient.DAY_OFF;
        }

        switch (subType) {
            case AM_HALF_DAY_OFF -> {
                return AttendanceScheduleFeignClient.AM_DAY_OFF;
            }
            case PM_HALF_DAY_OFF -> {
                return AttendanceScheduleFeignClient.PM_DAY_OFF;
            }
            default -> {
                return AttendanceScheduleFeignClient.DAY_OFF;
            }
        }
    }
}
