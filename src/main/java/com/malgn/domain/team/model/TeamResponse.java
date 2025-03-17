package com.malgn.domain.team.model;

import java.util.List;

public record TeamResponse(Long id,
                           String name,
                           List<TeamUserResponse> teamUsers) {
}
