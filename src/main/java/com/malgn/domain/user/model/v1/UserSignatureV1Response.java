package com.malgn.domain.user.model.v1;

import lombok.Builder;

import com.malgn.domain.user.entity.UserSignature;
import com.malgn.domain.user.model.UserSignatureResponse;

@Builder
public record UserSignatureV1Response(Long id,
                                      String userUniqueId)
    implements UserSignatureResponse {

    public static UserSignatureResponse from(UserSignature entity) {
        return UserSignatureV1Response.builder()
            .id(entity.getId())
            .userUniqueId(entity.getUserUniqueId())
            .build();
    }
}
