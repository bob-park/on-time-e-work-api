package com.malgn.domain.document.model.v1;

import java.math.BigDecimal;

import lombok.Builder;

import com.malgn.domain.user.model.UserCompLeaveEntryResponse;

@Builder
public record UsedCompLeaveEntryResponse(Long id,
                                         UserCompLeaveEntryResponse compLeaveEntry,
                                         BigDecimal usedDays) {
}
