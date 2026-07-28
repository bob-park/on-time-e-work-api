package com.malgn.domain.user.model;

import com.malgn.domain.team.model.TeamResponse;

public record UserGroupResponse(String id,
                                boolean isLeader,
                                String description,
                                TeamResponse group) {
}
