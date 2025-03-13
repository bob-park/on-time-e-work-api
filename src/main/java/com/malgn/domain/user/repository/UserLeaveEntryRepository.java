package com.malgn.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.user.entity.UserLeaveEntry;
import com.malgn.domain.user.repository.query.UserLeaveEntryQueryRepository;

public interface UserLeaveEntryRepository extends JpaRepository<UserLeaveEntry, Long>, UserLeaveEntryQueryRepository {
}
