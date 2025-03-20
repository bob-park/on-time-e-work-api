package com.malgn.domain.user.model.v1;

import com.malgn.domain.user.model.SearchUserLeaveEntryRequest;

public record SearchUserLeaveEntryV1Request(Integer year)
    implements SearchUserLeaveEntryRequest {
}
