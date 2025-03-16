package com.malgn.domain.user.repository.query;

import java.util.Optional;

import com.malgn.domain.user.entity.UserCompLeaveEntry;

public interface UserCompLeaveEntryQueryRepository {

    Optional<UserCompLeaveEntry> getUserEntry(Long id, String userUniqueId);

}
