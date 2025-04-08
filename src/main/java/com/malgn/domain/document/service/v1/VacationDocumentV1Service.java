package com.malgn.domain.document.service.v1;

import static com.google.common.base.Preconditions.*;
import static com.malgn.domain.document.model.v1.VacationDocumentV1Response.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoField;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.NotSupportException;
import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.entity.type.VacationType;
import com.malgn.domain.document.model.CreateVacationDocumentRequest;
import com.malgn.domain.document.model.SearchVacationDocumentRequest;
import com.malgn.domain.document.model.VacationDocumentResponse;
import com.malgn.domain.document.model.v1.CreateVacationDocumentV1Request;
import com.malgn.domain.document.model.v1.UsedCompLeaveEntryV1Request;
import com.malgn.domain.document.model.v1.VacationDocumentV1Response;
import com.malgn.domain.document.provider.RequestDocumentProvider;
import com.malgn.domain.document.provider.v1.DocumentV1Request;
import com.malgn.domain.document.repository.VacationDocumentRepository;
import com.malgn.domain.document.service.VacationDocumentService;
import com.malgn.domain.team.feign.TeamFeignClient;
import com.malgn.domain.team.model.TeamResponse;
import com.malgn.domain.user.entity.UserCompLeaveEntry;
import com.malgn.domain.user.entity.UserLeaveEntry;
import com.malgn.domain.user.entity.UserVacationUsedCompLeave;
import com.malgn.domain.user.feign.UserFeignClient;
import com.malgn.domain.user.model.UserResponse;
import com.malgn.domain.user.repository.UserCompLeaveEntryRepository;
import com.malgn.domain.user.repository.UserLeaveEntryRepository;
import com.malgn.domain.user.repository.UserVacationUsedCompLeaveRepository;
import com.malgn.domain.work.schedule.entity.WorkSchedule;
import com.malgn.domain.work.schedule.repository.WorkScheduleRepository;
import com.malgn.utils.AuthUtils;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class VacationDocumentV1Service implements VacationDocumentService {

    private static final List<Integer> DEFAULT_FAMILY_DAYS_WEEKS = List.of(1, 3);

    private final UserFeignClient userClient;
    private final TeamFeignClient teamClient;

    private final RequestDocumentProvider requestDocumentProvider;

    private final VacationDocumentRepository documentRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final UserLeaveEntryRepository userLeaveEntryRepository;
    private final UserCompLeaveEntryRepository userCompLeaveEntryRepository;
    private final UserVacationUsedCompLeaveRepository userVacationUsedCompLeaveRepository;
    private final VacationDocumentRepository vacationDocumentRepository;

    @Transactional
    @Override
    public VacationDocumentResponse createDocument(CreateVacationDocumentRequest createRequest) {

        CreateVacationDocumentV1Request createV1Request = (CreateVacationDocumentV1Request)createRequest;

        checkArgument(StringUtils.isNotBlank(createV1Request.userUniqueId()), "userUniqueId must be provided.");
        checkArgument(isNotEmpty(createV1Request.vacationType()), "vacationType must be provided.");

        String currentUserId = AuthUtils.getCurrentUserId();
        UserResponse user = userClient.getById(createV1Request.userUniqueId());
        TeamResponse team = teamClient.getTeamByUserUniqueId(user.uniqueId());

        checkArgument(StringUtils.equals(currentUserId, user.userId()), "not match user and account.");

        BigDecimal usedDays =
            calculateUsedDate(
                createV1Request.startDate(),
                createV1Request.endDate(),
                createV1Request.vacationSubType() != null);

        checkUserLeaveEntry(
            createV1Request.vacationType(),
            createV1Request.userUniqueId(),
            createV1Request.startDate().getYear(),
            usedDays);

        VacationDocument createdDocument =
            VacationDocument.builder()
                .userUniqueId(createV1Request.userUniqueId())
                .vacationType(createV1Request.vacationType())
                .vacationSubType(createV1Request.vacationSubType())
                .startDate(createV1Request.startDate())
                .endDate(createV1Request.endDate())
                .usedDays(usedDays)
                .reason(createV1Request.reason())
                .build();

        createdDocument = documentRepository.save(createdDocument);

        log.debug("created vacation document. ({})", createdDocument);

        // 보상 휴가 처리
        if (createV1Request.vacationType() == VacationType.COMPENSATORY) {
            checkArgument(!createV1Request.compLeaveEntries().isEmpty(), "compLeaveEntries must be provided.");

            // 보상 휴가 일수 확인
            BigDecimal tempUsedDays = usedDays.subtract(BigDecimal.ZERO);

            for (UsedCompLeaveEntryV1Request usedCompLeaveEntryRequest : createV1Request.compLeaveEntries()) {
                UserCompLeaveEntry userCompLeaveEntry =
                    userCompLeaveEntryRepository.getUserEntry(
                            usedCompLeaveEntryRequest.compLeaveEntryId(),
                            createV1Request.userUniqueId())
                        .orElseThrow(
                            () -> new NotFoundException(UserCompLeaveEntry.class, usedCompLeaveEntryRequest.compLeaveEntryId()));

                UserVacationUsedCompLeave createdUsedCompLeave =
                    UserVacationUsedCompLeave.builder()
                        .usedDays(usedCompLeaveEntryRequest.usedDays())
                        .build();

                createdDocument.addUsedCompLeave(createdUsedCompLeave);
                userCompLeaveEntry.addUsedCompLeave(createdUsedCompLeave);

                userVacationUsedCompLeaveRepository.save(createdUsedCompLeave);

                log.debug("used user vacation used comp leave entry id {}", usedCompLeaveEntryRequest.compLeaveEntryId());

                tempUsedDays = tempUsedDays.subtract(userCompLeaveEntry.availableDays());
            }

            checkArgument(tempUsedDays.compareTo(BigDecimal.ZERO) <= 0, "invalid compensatory leave count..");
        }

        requestDocumentProvider.request(
            DocumentV1Request.builder()
                .documentId(createdDocument.getId())
                .teamId(team.id())
                .documentType(DocumentType.VACATION)
                .build());

        return from(createdDocument);
    }

    @Override
    public Page<VacationDocumentResponse> search(SearchVacationDocumentRequest searchRequest, Pageable pageable) {

        Page<VacationDocument> result = vacationDocumentRepository.search(searchRequest, pageable);

        return result.map(VacationDocumentV1Response::from);
    }

    @Override
    public VacationDocumentResponse getById(Id<VacationDocument, Long> id) {

        VacationDocument document =
            vacationDocumentRepository.getDocumentById(id)
                .orElseThrow(() -> new NotFoundException(id));

        return from(document, true);
    }

    private BigDecimal calculateUsedDate(LocalDate startDate, LocalDate endDate, boolean isHalf) {

        if (isHalf) {
            return BigDecimal.valueOf(0.5);
        }

        BigDecimal result = BigDecimal.ZERO;

        int days = Period.between(startDate, endDate).getDays();

        List<WorkSchedule> holidays = workScheduleRepository.getAllClosedDays();

        for (int i = 0; i <= days; i++) {

            LocalDate tempDate = startDate.plusDays(i);

            if (isHoliday(holidays, tempDate)) {
                continue;
            }

            switch (tempDate.getDayOfWeek()) {
                case FRIDAY -> {
                    int weekCountOfMonth = tempDate.get(ChronoField.ALIGNED_WEEK_OF_MONTH);

                    if (DEFAULT_FAMILY_DAYS_WEEKS.contains(weekCountOfMonth)) {
                        result = result.add(BigDecimal.valueOf(0.5));
                    } else {
                        result = result.add(BigDecimal.ONE);
                    }
                }

                case SATURDAY, SUNDAY -> {
                    // ignore
                }

                default -> {
                    result = result.add(BigDecimal.ONE);
                }
            }

        }

        return result;
    }

    private void checkUserLeaveEntry(VacationType vacationType, String userUniqueId, int year, BigDecimal usedDays) {

        UserLeaveEntry userLeaveEntry =
            userLeaveEntryRepository.getLeaveEntry(userUniqueId, year)
                .orElseThrow(() -> new NotFoundException("userUniqueId: " + userUniqueId));

        BigDecimal availableDays = null;

        switch (vacationType) {
            case GENERAL -> availableDays = userLeaveEntry.availableDays();
            case COMPENSATORY -> availableDays = userLeaveEntry.availableCompDays();
            case OFFICIAL -> availableDays = usedDays.add(BigDecimal.ZERO);
            default -> throw new NotSupportException(vacationType.name());
        }

        checkArgument(availableDays.compareTo(usedDays) >= 0, "Please check your remaining leave days.");
    }

    private boolean isHoliday(List<WorkSchedule> holidays, LocalDate date) {
        for (WorkSchedule holiday : holidays) {

            LocalDate startDate = holiday.getStartDate();
            LocalDate endDate = holiday.getEndDate();

            if (holiday.isRepeated()) {
                // 반복인 경우 year 뺴고 계산하기
                startDate =
                    LocalDate.of(
                        date.getYear(),
                        holiday.getStartDate().getMonth(),
                        holiday.getStartDate().getDayOfMonth());

                endDate =
                    LocalDate.of(
                        date.getYear(),
                        holiday.getEndDate().getMonth(),
                        holiday.getEndDate().getDayOfMonth());

            }

            if (startDate.compareTo(date) <= 0 && endDate.compareTo(date) >= 0) {
                return true;
            }

        }

        return false;
    }
}
