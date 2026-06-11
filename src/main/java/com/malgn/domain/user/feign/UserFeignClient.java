package com.malgn.domain.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.malgn.common.model.SimplePageImpl;
import com.malgn.domain.user.model.UserResponse;
import com.malgn.domain.user.model.UserSearchRequest;

@FeignClient(name = "auth-user-api", contextId = "auth-user-api")
public interface UserFeignClient {

    @GetMapping(path = "api/v1/users/{uniqueId}/summary")
    UserResponse getById(@PathVariable String uniqueId);

    @GetMapping(path = "api/v1/users/summaries")
    SimplePageImpl<UserResponse> getAll(@SpringQueryMap UserSearchRequest searchRequest, Pageable pageable);
}
