package com.malgn.domain.user.model.v1;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;

import com.malgn.domain.user.model.UserCompLeaveEntryResponse;
import com.malgn.domain.user.entity.UserCompLeaveEntry;

@Builder
public record UserCompLeaveEntryV1Response(Long id,
                                           String contents,
                                           String description,
                                           LocalDate effectiveDate,
                                           BigDecimal leaveDays,
                                           BigDecimal usedDays,
                                           LocalDateTime createdDate,
                                           String createdBy,
                                           LocalDateTime lastModifiedDate,
                                           String lastModifiedBy)
    implements UserCompLeaveEntryResponse {

    public static UserCompLeaveEntryResponse from(UserCompLeaveEntry entry) {
        return UserCompLeaveEntryV1Response.builder()
            .id(entry.getId())
            .contents(entry.getContents())
            .description(entry.getDescription())
            .effectiveDate(entry.getEffectiveDate())
            .leaveDays(entry.getLeaveDays())
            .usedDays(entry.getUsedDays())
            .createdDate(entry.getCreatedDate())
            .createdBy(entry.getCreatedBy())
            .lastModifiedDate(entry.getLastModifiedDate())
            .lastModifiedBy(entry.getLastModifiedBy())
            .build();
    }
}
