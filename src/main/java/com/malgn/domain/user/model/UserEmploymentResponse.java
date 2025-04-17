package com.malgn.domain.user.model;

import java.time.LocalDateTime;

import com.malgn.common.model.CommonResponse;
import com.malgn.domain.user.entity.type.EmployStatus;

public interface UserEmploymentResponse extends CommonResponse {

    Long id();

    String userUniqueId();

    LocalDateTime effectiveDate();

    EmployStatus status();

}
