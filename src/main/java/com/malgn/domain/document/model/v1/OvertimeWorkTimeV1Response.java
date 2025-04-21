package com.malgn.domain.document.model.v1;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

import com.malgn.domain.document.entity.OvertimeWorkTime;
import com.malgn.domain.document.model.OvertimeWorkTimeReportResponse;
import com.malgn.domain.document.model.OvertimeWorkTimeResponse;

@Builder
public record OvertimeWorkTimeV1Response(Long id,
                                         LocalDateTime startDate,
                                         LocalDateTime endDate,
                                         BigDecimal appliedHours,
                                         String userUniqueId,
                                         String username,
                                         String contents,
                                         Boolean isDayOff,
                                         Boolean isExtraPayment,
                                         BigDecimal appliedExtraPaymentHours,
                                         List<OvertimeWorkTimeReportResponse> reports)
    implements OvertimeWorkTimeResponse {

    public static OvertimeWorkTimeResponse from(OvertimeWorkTime entity) {
        return OvertimeWorkTimeV1Response.builder()
            .id(entity.getId())
            .startDate(entity.getStartDate())
            .endDate(entity.getEndDate())
            .appliedHours(entity.getAppliedHours())
            .userUniqueId(entity.getUserUniqueId())
            .username(entity.getUsername())
            .contents(entity.getContents())
            .isDayOff(entity.getIsDayOff())
            .isExtraPayment(entity.getIsExtraPayment())
            .appliedExtraPaymentHours(entity.getAppliedExtraPaymentHours())
            .reports(
                entity.getReports().stream()
                    .map(OvertimeWorkTimeReportV1Response::from)
                    .toList())
            .build();
    }

}
