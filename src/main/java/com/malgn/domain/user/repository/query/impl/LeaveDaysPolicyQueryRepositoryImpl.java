package com.malgn.domain.user.repository.query.impl;

import static com.malgn.domain.user.entity.QLeaveDaysPolicy.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.domain.user.entity.LeaveDaysPolicy;
import com.malgn.domain.user.entity.QLeaveDaysPolicy;
import com.malgn.domain.user.repository.query.LeaveDaysPolicyQueryRepository;

@RequiredArgsConstructor
public class LeaveDaysPolicyQueryRepositoryImpl implements LeaveDaysPolicyQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<LeaveDaysPolicy> getPolicyAll() {
        return query.selectFrom(leaveDaysPolicy)
            .orderBy(leaveDaysPolicy.continuousYears.asc())
            .fetch();
    }
}
