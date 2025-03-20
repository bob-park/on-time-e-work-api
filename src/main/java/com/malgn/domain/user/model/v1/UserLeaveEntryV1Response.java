package com.malgn.domain.user.model.v1;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

import com.malgn.domain.user.entity.UserLeaveEntry;
import com.malgn.domain.user.model.UserLeaveEntryResponse;

@Builder
public record UserLeaveEntryV1Response(Long id,
                                       String userUniqueId,
                                       Integer year,
                                       BigDecimal totalLeaveDays,
                                       BigDecimal usedLeaveDays,
                                       BigDecimal totalCompLeaveDays,
                                       BigDecimal usedCompLeaveDays,
                                       LocalDateTime createdDate,
                                       LocalDateTime lastModifiedDate)
    implements UserLeaveEntryResponse {

    public static UserLeaveEntryResponse from(UserLeaveEntry entity) {
        return UserLeaveEntryV1Response.builder()
            .id(entity.getId())
            .userUniqueId(entity.getUserUniqueId())
            .year(entity.getYear())
            .totalLeaveDays(entity.getTotalLeaveDays())
            .usedLeaveDays(entity.getUsedLeaveDays())
            .totalCompLeaveDays(entity.getTotalCompLeaveDays())
            .usedCompLeaveDays(entity.getUsedCompLeaveDays())
            .createdDate(entity.getCreatedDate())
            .lastModifiedDate(entity.getLastModifiedDate())
            .build();
    }
}
