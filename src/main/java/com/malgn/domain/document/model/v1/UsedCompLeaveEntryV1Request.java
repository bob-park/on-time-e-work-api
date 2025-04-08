package com.malgn.domain.document.model.v1;

import java.math.BigDecimal;

import com.malgn.domain.document.model.UsedCompLeaveEntryRequest;

public record UsedCompLeaveEntryV1Request(Long compLeaveEntryId,
                                          BigDecimal usedDays)
    implements UsedCompLeaveEntryRequest {
}
