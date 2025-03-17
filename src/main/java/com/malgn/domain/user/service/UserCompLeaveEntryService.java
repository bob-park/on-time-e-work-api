package com.malgn.domain.user.service;

import java.util.List;

import com.malgn.domain.user.model.SearchUserCompLeaveEntryRequest;
import com.malgn.domain.user.model.UserCompLeaveEntryResponse;

public interface UserCompLeaveEntryService {

    List<UserCompLeaveEntryResponse> getAll(String userUniqueId, SearchUserCompLeaveEntryRequest searchRequest);

}
