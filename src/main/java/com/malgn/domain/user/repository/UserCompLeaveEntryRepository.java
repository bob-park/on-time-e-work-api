package com.malgn.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.user.entity.UserCompLeaveEntry;
import com.malgn.domain.user.repository.query.UserCompLeaveEntryQueryRepository;

public interface UserCompLeaveEntryRepository extends JpaRepository<UserCompLeaveEntry, Long>,
    UserCompLeaveEntryQueryRepository {
}
