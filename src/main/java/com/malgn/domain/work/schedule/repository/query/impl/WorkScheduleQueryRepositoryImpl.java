package com.malgn.domain.work.schedule.repository.query.impl;

import static com.malgn.domain.work.schedule.entity.QWorkSchedule.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.domain.work.schedule.entity.QWorkSchedule;
import com.malgn.domain.work.schedule.entity.WorkSchedule;
import com.malgn.domain.work.schedule.repository.query.WorkScheduleQueryRepository;

@RequiredArgsConstructor
public class WorkScheduleQueryRepositoryImpl implements WorkScheduleQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<WorkSchedule> getAllClosedDays() {
        return query.selectFrom(workSchedule)
            .where(workSchedule.isClosed.eq(true))
            .orderBy(workSchedule.startDate.asc())
            .fetch();
    }
}
