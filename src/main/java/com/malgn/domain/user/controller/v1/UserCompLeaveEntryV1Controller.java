package com.malgn.domain.user.controller.v1;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.domain.user.model.UserCompLeaveEntryResponse;
import com.malgn.domain.user.model.v1.SearchUserCompLeaveEntryV1Request;
import com.malgn.domain.user.service.v1.UserCompLeaveEntryV1Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/users/{userUniqueId}/comp/leave/entries")
public class UserCompLeaveEntryV1Controller {

    private final UserCompLeaveEntryV1Service userCompLeaveEntryService;

    @GetMapping(path = "")
    public List<UserCompLeaveEntryResponse> getAll(@PathVariable String userUniqueId,
        SearchUserCompLeaveEntryV1Request searchRequest) {
        return userCompLeaveEntryService.getAll(userUniqueId, searchRequest);
    }

}
