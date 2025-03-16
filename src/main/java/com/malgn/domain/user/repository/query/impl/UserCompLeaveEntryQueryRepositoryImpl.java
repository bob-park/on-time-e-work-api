package com.malgn.domain.user.repository.query.impl;

import static com.malgn.domain.user.entity.QUserCompLeaveEntry.*;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.domain.user.entity.QUserCompLeaveEntry;
import com.malgn.domain.user.entity.UserCompLeaveEntry;
import com.malgn.domain.user.repository.query.UserCompLeaveEntryQueryRepository;

@RequiredArgsConstructor
public class UserCompLeaveEntryQueryRepositoryImpl implements UserCompLeaveEntryQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<UserCompLeaveEntry> getUserEntry(Long id, String userUniqueId) {
        return Optional.ofNullable(
            query.selectFrom(userCompLeaveEntry)
                .where(
                    userCompLeaveEntry.id.eq(id),
                    userCompLeaveEntry.userUniqueId.eq(userUniqueId),
                    userCompLeaveEntry.leaveDays.gt(userCompLeaveEntry.usedDays))
                .fetchOne());
    }
}
