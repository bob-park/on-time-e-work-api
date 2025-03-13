package com.malgn.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.user.entity.UserEmployment;
import com.malgn.domain.user.repository.query.UserEmploymentQueryRepository;

public interface UserEmploymentRepository extends JpaRepository<UserEmployment, Long>, UserEmploymentQueryRepository {
}
