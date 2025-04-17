package com.malgn.domain.user.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.domain.user.model.UserEmploymentResponse;
import com.malgn.domain.user.model.v1.SearchUserEmployV1Request;
import com.malgn.domain.user.service.v1.UserEmploymentV1Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/users/employments")
public class UserEmploymentV1Controller {

    private final UserEmploymentV1Service userEmploymentService;

    @GetMapping(path = "")
    public Page<UserEmploymentResponse> search(SearchUserEmployV1Request searchRequest,
        @PageableDefault(size = 25, direction = Direction.DESC, sort = "createdDate") Pageable pageable) {
        return userEmploymentService.search(searchRequest, pageable);
    }

}
