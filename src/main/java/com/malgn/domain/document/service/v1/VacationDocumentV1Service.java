package com.malgn.domain.document.service.v1;

import static com.google.common.base.Preconditions.*;
import static com.malgn.domain.document.model.v1.VacationDocumentV1Response.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoField;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.NotSupportException;
import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.entity.type.VacationType;
import com.malgn.domain.document.model.CreateVacationDocumentRequest;
import com.malgn.domain.document.model.VacationDocumentResponse;
import com.malgn.domain.document.model.v1.CreateVacationDocumentV1Request;
import com.malgn.domain.document.repository.VacationDocumentRepository;
import com.malgn.domain.document.service.VacationDocumentService;
import com.malgn.domain.user.entity.UserLeaveEntry;
import com.malgn.domain.user.feign.UserFeignClient;
import com.malgn.domain.user.model.UserResponse;
import com.malgn.domain.user.repository.UserLeaveEntryRepository;
import com.malgn.domain.work.schedule.entity.WorkSchedule;
import com.malgn.domain.work.schedule.repository.WorkScheduleRepository;
import com.malgn.utils.AuthUtils;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class VacationDocumentV1Service implements VacationDocumentService {

    private static final List<Integer> DEFAULT_FAMILY_DAYS_WEEKS = List.of(1, 3);
    private static final List<Integer> DEFAULT_WEEK_ENDS_ = List.of(3, 7);

    private final UserFeignClient userClient;

    private final VacationDocumentRepository documentRepository;
    private final UserLeaveEntryRepository userLeaveEntryRepository;
    private final WorkScheduleRepository workScheduleRepository;

    @Transactional
    @Override
    public VacationDocumentResponse createDocument(CreateVacationDocumentRequest createRequest) {

        CreateVacationDocumentV1Request createV1Request = (CreateVacationDocumentV1Request)createRequest;

        checkArgument(StringUtils.isNotBlank(createV1Request.userUniqueId()), "userUniqueId must be provided.");

        String currentUserId = AuthUtils.getCurrentUserId();
        UserResponse user = userClient.getById(createV1Request.userUniqueId());

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

        log.debug("created vacation document: {}", createdDocument);

        return from(createdDocument);
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
