package com.malgn.domain.attendance.model;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record AddAttendanceScheduleRequest(String userUniqueId,
                                           LocalDate workingDate,
                                           String dayOffType) {
}
