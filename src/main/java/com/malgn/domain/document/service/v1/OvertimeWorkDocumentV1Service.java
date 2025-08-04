package com.malgn.domain.document.service.v1;

import static com.google.common.base.Preconditions.*;
import static com.malgn.domain.document.model.v1.OvertimeWorkDocumentV1Response.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.OvertimeWorkDocument;
import com.malgn.domain.document.entity.OvertimeWorkTime;
import com.malgn.domain.document.entity.OvertimeWorkTimeReport;
import com.malgn.domain.document.model.CreateOverTimeWorkTimeRequest;
import com.malgn.domain.document.model.CreateOvertimeWorkDocumentRequest;
import com.malgn.domain.document.model.OvertimeWorkDocumentResponse;
import com.malgn.domain.document.model.v1.CreateOverTimeWorkTimeV1Request;
import com.malgn.domain.document.model.v1.CreateOvertimeWorkDocumentV1Request;
import com.malgn.domain.document.repository.OvertimeWorkDocumentRepository;
import com.malgn.domain.document.repository.OvertimeWorkTimeReportRepository;
import com.malgn.domain.document.repository.OvertimeWorkTimeRepository;
import com.malgn.domain.document.service.OvertimeWorkDocumentService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OvertimeWorkDocumentV1Service implements OvertimeWorkDocumentService {

    /**
     * policy
     */
    // 식사 시간 - 기존 시간 -1
    private static final LocalTime LUNCH_START_TIME = LocalTime.of(12, 0);
    private static final LocalTime LUNCH_END_TIME = LocalTime.of(13, 0);
    private static final LocalTime DINNER_START_TIME = LocalTime.of(18, 0);
    private static final LocalTime DINNER_END_TIME = LocalTime.of(19, 0);

    // 야간 작업 - 기존 시간 + (해당 시간 * 0.5)
    private static final LocalTime NIGHT_WORK_START_TIME = LocalTime.of(22, 0);
    private static final LocalTime NIGHT_WORK_END_TIME = LocalTime.of(6, 0);

    // 가중 작업 기준 시간 - 기존 시간 + (초과된 시간 * 0.5)
    private static final long STANDARD_WORK_HOURS = 8;

    // 보상 휴가 단위
    private static final int DEFAULT_COMPENSATORY_HOURS = 4;

    /**
     * repository
     */
    private final OvertimeWorkDocumentRepository overtimeWorkRepository;
    private final OvertimeWorkTimeRepository workTimeRepository;
    private final OvertimeWorkTimeReportRepository workTimeReportRepository;

    @Transactional
    @Override
    public OvertimeWorkDocumentResponse create(CreateOvertimeWorkDocumentRequest createRequest) {

        CreateOvertimeWorkDocumentV1Request createV1Request = (CreateOvertimeWorkDocumentV1Request)createRequest;

        checkArgument(StringUtils.isNotBlank(createV1Request.userUniqueId()), "userUniqueId must be provided.");
        checkArgument(!createV1Request.times().isEmpty(), "times must be provided.");

        OvertimeWorkDocument createdDocument =
            OvertimeWorkDocument.builder()
                .userUniqueId(createV1Request.userUniqueId())
                .build();

        createdDocument = overtimeWorkRepository.save(createdDocument);

        log.debug("created overtime work document. ({})", createdDocument);

        // calculate work time
        for (CreateOverTimeWorkTimeV1Request time : createV1Request.times()) {
            calculateWorkTime(createdDocument, time);
        }

        return from(createdDocument);
    }

    @Override
    public OvertimeWorkDocumentResponse getById(Id<OvertimeWorkDocument, Long> id) {

        OvertimeWorkDocument document =
            overtimeWorkRepository.findDocument(id)
                .orElseThrow(() -> new NotFoundException(id));

        return from(document);
    }

    private OvertimeWorkTime calculateWorkTime(OvertimeWorkDocument document,
        CreateOverTimeWorkTimeRequest createRequest) {

        CreateOverTimeWorkTimeV1Request createV1Request = (CreateOverTimeWorkTimeV1Request)createRequest;

        checkArgument(isNotEmpty(createV1Request.startDate()), "startDate must be provided.");
        checkArgument(isNotEmpty(createV1Request.endDate()), "endDate must be provided.");
        checkArgument(createV1Request.startDate().isBefore(createV1Request.endDate()),
            "startDate must be before endDate.");

        OvertimeWorkTime createdWorkTime =
            OvertimeWorkTime.builder()
                .startDate(createV1Request.startDate())
                .endDate(createV1Request.endDate())
                .contents(createV1Request.contents())
                .userUniqueId(createV1Request.userUniqueId())
                .username(createV1Request.username())
                .isDayOff(createV1Request.isDayOff())
                .isExtraPayment(createV1Request.isExtraPayment())
                .build();

        document.addTime(createdWorkTime);

        createdWorkTime = workTimeRepository.save(createdWorkTime);

        log.debug("created overtime work time. ({})", createdWorkTime);

        // applied hours
        BigDecimal appliedHours =
            calculateHours(createdWorkTime, createV1Request.startDate(), createV1Request.endDate());

        // applied extra payment hours
        BigDecimal appliedExtraPaymentHours =
            createV1Request.isDayOff() ? calculateExtraPaymentHours(appliedHours) : appliedHours.add(BigDecimal.ZERO);

        createdWorkTime.updateAppliedHours(appliedHours);
        createdWorkTime.updateAppliedExtraPaymentHours(appliedExtraPaymentHours);

        return createdWorkTime;
    }

    private BigDecimal calculateHours(OvertimeWorkTime workTime, LocalDateTime startDateTime,
        LocalDateTime endDateTime) {

        LocalDate workDate = startDateTime.toLocalDate();

        StringBuilder report = new StringBuilder();

        Duration duration = Duration.between(startDateTime, endDateTime);

        long workMinutes = duration.toMinutes();
        BigDecimal workHours = BigDecimal.valueOf(workMinutes / 60.0);

        // 식사시간 여부
        if (startDateTime.isBefore(workDate.atTime(LUNCH_START_TIME)) &&
            endDateTime.isAfter(workDate.atTime(LUNCH_END_TIME))) {
            workHours = workHours.subtract(BigDecimal.ONE);

            report.append("점심 시간 적용: -1h");
        }

        // if (startDateTime.isAfter(workDate.atTime(DINNER_START_TIME)) &&
        //     endDateTime.isBefore(workDate.atTime(DINNER_END_TIME))) {
        //     workHours = workHours.subtract(BigDecimal.ONE);
        //
        //     report.append("저녁 시간 적용: -1h");
        // }

        // 총 시간
        report.append("근무 시간: ").append(workHours).append("\n");

        // 초과 근무 적용
        BigDecimal overHoursResult = BigDecimal.ZERO;

        if (workHours.compareTo(BigDecimal.valueOf(STANDARD_WORK_HOURS)) > 0) {
            BigDecimal overHours = workHours.subtract(BigDecimal.valueOf(STANDARD_WORK_HOURS));
            overHoursResult = overHours.multiply(BigDecimal.valueOf(0.5));
            report.append("초과 근무: ").append(overHours).append(" * 0.5 = ").append(overHoursResult).append("\n");

            // workHours = workHours.add(result);
        }

        // 휴일 시간 적용
        BigDecimal holyDayWorkHours = workHours.multiply(BigDecimal.valueOf(0.5));
        report.append("휴일 근무: ").append(workHours).append(" * 0.5 = ").append(holyDayWorkHours).append("\n");

        workHours = workHours.add(holyDayWorkHours);

        // 야간 작업 시간 적용
        // 익일 여부
        boolean isNextDay = startDateTime.toLocalDate().isBefore(endDateTime.toLocalDate());

        BigDecimal nightHours = BigDecimal.ZERO;

        if (isNextDay) {
            // 익일인 경우
            LocalDateTime nightStartTime = max(workDate.atTime(NIGHT_WORK_START_TIME), startDateTime);
            LocalDateTime nightEndTime = min(workDate.plusDays(1).atTime(NIGHT_WORK_END_TIME), endDateTime);

            Duration nightDuration = Duration.between(nightStartTime, nightEndTime);

            nightHours = BigDecimal.valueOf(nightDuration.toMinutes() / 60.0);
        } else {
            // 당일인 경우
            LocalDateTime nightStartTime = min(workDate.atTime(NIGHT_WORK_START_TIME), startDateTime);
            LocalDateTime nightEndTime = min(workDate.atTime(NIGHT_WORK_END_TIME), endDateTime);

            Duration nightDuration = Duration.between(nightStartTime, nightEndTime);

            // 음수인 경우 처리하지 않음
            if (nightDuration.toHours() > 0) {
                nightHours = BigDecimal.valueOf(nightDuration.toMinutes() / 60.0);
            }
        }

        if (nightHours.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal result = nightHours.multiply(BigDecimal.valueOf(0.5));

            report.append("야간 근무: ")
                .append(nightHours)
                .append(" * 0.5 = ")
                .append(result)
                .append("\n");

            workHours = workHours.add(result);
        }

        workHours = workHours.add(overHoursResult);

        report.append("총: ").append(workHours);

        OvertimeWorkTimeReport createdReport =
            OvertimeWorkTimeReport.builder()
                .report(report.toString())
                .build();

        workTime.addReport(createdReport);

        createdReport = workTimeReportRepository.save(createdReport);

        log.debug("created overtime work time report. ({})", createdReport);

        return workHours;
    }

    private BigDecimal calculateExtraPaymentHours(BigDecimal hours) {

        BigDecimal result = hours.add(BigDecimal.ZERO);

        long temp = hours.longValue() / DEFAULT_COMPENSATORY_HOURS;

        BigDecimal subjectValue = BigDecimal.valueOf(temp * DEFAULT_COMPENSATORY_HOURS);

        return result.subtract(subjectValue);
    }
}
