package com.malgn.domain.user.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.domain.user.model.UserLeaveEntryResponse;
import com.malgn.domain.user.model.v1.SearchUserLeaveEntryV1Request;
import com.malgn.domain.user.service.v1.UserLeaveEntryV1Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/users/{userUniqueId}/leave/entries")
public class UserLeaveEntryV1Controller {

    private final UserLeaveEntryV1Service userLeaveEntryService;

    @GetMapping(path = "")
    public UserLeaveEntryResponse getLeaveEntry(@PathVariable String userUniqueId,
        SearchUserLeaveEntryV1Request searchRequest) {
        return userLeaveEntryService.getLeaveEntry(userUniqueId, searchRequest);
    }

}
