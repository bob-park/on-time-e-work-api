package com.malgn.domain.team.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.malgn.domain.team.model.TeamResponse;

@FeignClient(name = "on-time-api", contextId = "on-time-api-team")
public interface TeamFeignClient {

    @GetMapping(path = "api/v1/users/{userUniqueId}/team")
    TeamResponse getTeamByUserUniqueId(@PathVariable String userUniqueId);
}
