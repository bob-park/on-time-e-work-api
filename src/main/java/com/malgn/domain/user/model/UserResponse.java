package com.malgn.domain.user.model;

import java.util.List;

import lombok.Builder;

@Builder
public record UserResponse(String id,
                           String userId,
                           String username,
                           List<UserGroupResponse> groups,
                           PositionResponse position) {
}
