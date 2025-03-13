package com.malgn.domain.user.repository.query.impl;

import static com.malgn.domain.user.entity.QUserLeaveEntry.*;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.domain.user.entity.UserLeaveEntry;
import com.malgn.domain.user.repository.query.UserLeaveEntryQueryRepository;

@RequiredArgsConstructor
public class UserLeaveEntryQueryRepositoryImpl implements UserLeaveEntryQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<UserLeaveEntry> getLeaveEntry(String userId, int year) {
        return Optional.ofNullable(
            query.selectFrom(userLeaveEntry)
                .where(
                    userLeaveEntry.userUniqueId.eq(userId),
                    userLeaveEntry.year.eq(year))
                .fetchOne());
    }
}
