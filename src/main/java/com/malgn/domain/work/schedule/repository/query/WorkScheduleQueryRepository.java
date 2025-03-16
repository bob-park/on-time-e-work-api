package com.malgn.domain.work.schedule.repository.query;

import java.util.List;

import com.malgn.domain.work.schedule.entity.WorkSchedule;

public interface WorkScheduleQueryRepository {

    List<WorkSchedule> getAllClosedDays();

}
