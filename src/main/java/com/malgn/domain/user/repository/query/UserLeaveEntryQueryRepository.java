package com.malgn.domain.user.repository.query;

import java.util.Optional;

import com.malgn.domain.user.entity.UserLeaveEntry;

public interface UserLeaveEntryQueryRepository {

    Optional<UserLeaveEntry> getLeaveEntry(String userId, int year);

}
