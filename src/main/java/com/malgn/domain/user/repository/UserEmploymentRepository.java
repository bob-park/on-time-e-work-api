package com.malgn.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.user.entity.UserEmployment;
import com.malgn.domain.user.repository.query.UserEmploymentQueryRepository;

public interface UserEmploymentRepository extends JpaRepository<UserEmployment, Long>, UserEmploymentQueryRepository {

    Optional<UserEmployment> findByUserUniqueId(String userUniqueId);


}
