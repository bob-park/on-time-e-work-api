package com.malgn.domain.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class UserSearchRequest {

    private Boolean isDeleted;

    @Builder
    private UserSearchRequest(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
