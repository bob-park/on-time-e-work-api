package com.malgn.domain.work.schedule.model.v1;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;

import com.malgn.domain.work.schedule.entity.WorkSchedule;
import com.malgn.domain.work.schedule.model.WorkScheduleResponse;

@Builder
public record WorkScheduleV1Response(Long id,
                                     String contents,
                                     String description,
                                     LocalDate startDate,
                                     LocalDate endDate,
                                     Boolean isRepeated,
                                     Boolean isClosed,
                                     LocalDateTime createdDate,
                                     String createdBy,
                                     LocalDateTime lastModifiedDate,
                                     String lastModifiedBy)
    implements WorkScheduleResponse {

    public static WorkScheduleResponse from(WorkSchedule workSchedule) {
        return WorkScheduleV1Response.builder()
            .id(workSchedule.getId())
            .contents(workSchedule.getContents())
            .description(workSchedule.getDescription())
            .startDate(workSchedule.getStartDate())
            .endDate(workSchedule.getEndDate())
            .isRepeated(workSchedule.getIsRepeated())
            .isClosed(workSchedule.getIsClosed())
            .createdDate(workSchedule.getCreatedDate())
            .createdBy(workSchedule.getCreatedBy())
            .lastModifiedDate(workSchedule.getLastModifiedDate())
            .lastModifiedBy(workSchedule.getLastModifiedBy())
            .build();
    }
}
