package com.malgn.domain.user.model.v1;

import java.time.LocalDateTime;

import com.malgn.domain.user.entity.type.EmployStatus;
import com.malgn.domain.user.model.SearchUserEmploymentRequest;

public record SearchUserEmploymentV1Request(String userUniqueId,
                                            LocalDateTime effectiveDateFrom,
                                            LocalDateTime effectiveDateTo,
                                            EmployStatus status)
    implements SearchUserEmploymentRequest {
}
