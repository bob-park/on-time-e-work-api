package com.malgn.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.user.entity.LeaveDaysPolicy;
import com.malgn.domain.user.repository.query.LeaveDaysPolicyQueryRepository;

public interface LeaveDaysPolicyRepository extends JpaRepository<LeaveDaysPolicy, Long>,
    LeaveDaysPolicyQueryRepository {
}
