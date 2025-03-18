package com.malgn.domain.attendance.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.malgn.domain.attendance.model.AddAttendanceScheduleRequest;

@FeignClient(name = "on-time-api", contextId = "on-time-api-attendance-schedule")
public interface AttendanceScheduleFeignClient {

    String DAY_OFF = "DAY_OFF";
    String AM_DAY_OFF = "AM_HALF_DAY_OFF";
    String PM_DAY_OFF = "PM_HALF_DAY_OFF";

    @PostMapping(path = "api/v1/attendance/schedules")
    void addSchedule(@RequestBody AddAttendanceScheduleRequest addRequest);
}
