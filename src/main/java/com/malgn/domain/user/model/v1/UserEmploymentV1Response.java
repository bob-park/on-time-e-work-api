package com.malgn.domain.user.model.v1;

import java.time.LocalDateTime;

import lombok.Builder;

import com.malgn.domain.user.entity.UserEmployment;
import com.malgn.domain.user.entity.type.EmployStatus;
import com.malgn.domain.user.model.UserEmploymentResponse;

@Builder
public record UserEmploymentV1Response(Long id,
                                       String userUniqueId,
                                       LocalDateTime effectiveDate,
                                       EmployStatus status,
                                       LocalDateTime createdDate,
                                       String createdBy,
                                       LocalDateTime lastModifiedDate,
                                       String lastModifiedBy)
    implements UserEmploymentResponse {

    public static UserEmploymentResponse from(UserEmployment entity) {
        return UserEmploymentV1Response.builder()
            .id(entity.getId())
            .userUniqueId(entity.getUserUniqueId())
            .effectiveDate(entity.getEffectiveDate())
            .status(entity.getStatus())
            .createdDate(entity.getCreatedDate())
            .createdBy(entity.getCreatedBy())
            .lastModifiedDate(entity.getLastModifiedDate())
            .lastModifiedBy(entity.getLastModifiedBy())
            .build();
    }

}
