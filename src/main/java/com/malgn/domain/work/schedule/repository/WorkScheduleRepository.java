package com.malgn.domain.work.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.work.schedule.entity.WorkSchedule;
import com.malgn.domain.work.schedule.repository.query.WorkScheduleQueryRepository;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long>, WorkScheduleQueryRepository {
}
