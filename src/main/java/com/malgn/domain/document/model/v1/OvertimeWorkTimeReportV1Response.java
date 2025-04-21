package com.malgn.domain.document.model.v1;

import lombok.Builder;

import com.malgn.domain.document.entity.OvertimeWorkTimeReport;
import com.malgn.domain.document.model.OvertimeWorkTimeReportResponse;

@Builder
public record OvertimeWorkTimeReportV1Response(Long id,
                                               String report)
    implements OvertimeWorkTimeReportResponse {

    public static OvertimeWorkTimeReportResponse from(OvertimeWorkTimeReport entity) {
        return OvertimeWorkTimeReportV1Response.builder()
            .report(entity.getReport())
            .build();
    }
}
