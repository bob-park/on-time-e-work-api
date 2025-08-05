package com.malgn.domain.user.model;

import lombok.Builder;

import com.malgn.domain.team.model.TeamResponse;

@Builder
public record UserResponse(String id,
                           String userId,
                           String username,
                           TeamResponse team,
                           PositionResponse position) {
}
