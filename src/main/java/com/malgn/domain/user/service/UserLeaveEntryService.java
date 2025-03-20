package com.malgn.domain.user.service;

import com.malgn.domain.user.model.SearchUserLeaveEntryRequest;
import com.malgn.domain.user.model.UserLeaveEntryResponse;

public interface UserLeaveEntryService {

    UserLeaveEntryResponse getLeaveEntry(String userUniqueId, SearchUserLeaveEntryRequest searchRequest);

}
