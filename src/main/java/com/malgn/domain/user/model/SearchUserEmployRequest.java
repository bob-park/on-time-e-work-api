package com.malgn.domain.user.model;

import java.time.LocalDateTime;

import com.malgn.domain.user.entity.type.EmployStatus;

public interface SearchUserEmployRequest {

    String userUniqueId();

    LocalDateTime effectiveDateFrom();

    LocalDateTime effectiveDateTo();

    EmployStatus status();

}
