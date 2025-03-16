package com.malgn.domain.work.schedule.service;

import com.malgn.domain.work.schedule.model.CreateWorkScheduleRequest;
import com.malgn.domain.work.schedule.model.WorkScheduleResponse;

public interface WorkScheduleService {

    WorkScheduleResponse createSchedule(CreateWorkScheduleRequest createRequest);

}
