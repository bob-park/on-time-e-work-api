package com.malgn.domain.user.model.v1;

import static org.apache.commons.lang3.ObjectUtils.*;

import lombok.Builder;

import com.malgn.domain.user.model.SearchUserCompLeaveEntryRequest;

@Builder
public record SearchUserCompLeaveEntryV1Request(Boolean isAvailable)
    implements SearchUserCompLeaveEntryRequest {

    public SearchUserCompLeaveEntryV1Request {
        isAvailable = defaultIfNull(isAvailable, true);
    }

}
