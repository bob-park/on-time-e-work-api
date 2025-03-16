package com.malgn.domain.work.schedule.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.domain.work.schedule.model.WorkScheduleResponse;
import com.malgn.domain.work.schedule.model.v1.CreateWorkScheduleV1Request;
import com.malgn.domain.work.schedule.service.v1.WorkScheduleV1Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/work/schedules")
public class WorkScheduleV1Controller {

    private final WorkScheduleV1Service workScheduleService;

    @PreAuthorize("hasRole('MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public WorkScheduleResponse createSchedule(@RequestBody CreateWorkScheduleV1Request createRequest) {
        return workScheduleService.createSchedule(createRequest);
    }
}
