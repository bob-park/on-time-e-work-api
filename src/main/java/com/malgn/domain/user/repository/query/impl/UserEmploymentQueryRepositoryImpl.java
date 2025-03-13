package com.malgn.domain.user.repository.query.impl;

import static com.malgn.domain.user.entity.QUserEmployment.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.domain.user.entity.UserEmployment;
import com.malgn.domain.user.entity.type.EmployStatus;
import com.malgn.domain.user.repository.query.UserEmploymentQueryRepository;

@RequiredArgsConstructor
public class UserEmploymentQueryRepositoryImpl implements UserEmploymentQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<UserEmployment> getActiveAll() {
        return query.selectFrom(userEmployment)
            .where(userEmployment.status.eq(EmployStatus.ACTIVE))
            .orderBy(userEmployment.effectiveDate.asc())
            .fetch();
    }
}
