package com.malgn.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.user.entity.UserSignature;

public interface UserSignatureRepository extends JpaRepository<UserSignature, Long> {

    Optional<UserSignature> findByUserUniqueId(String userUniqueId);
}
