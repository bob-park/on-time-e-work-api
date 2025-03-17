package com.malgn.domain.user.repository.query;

import java.util.List;
import java.util.Optional;

import com.malgn.domain.user.entity.UserCompLeaveEntry;
import com.malgn.domain.user.model.SearchUserCompLeaveEntryRequest;

public interface UserCompLeaveEntryQueryRepository {

    Optional<UserCompLeaveEntry> getUserEntry(Long id, String userUniqueId);

    List<UserCompLeaveEntry> getAll(String userUniqueId, SearchUserCompLeaveEntryRequest searchRequest);

}
