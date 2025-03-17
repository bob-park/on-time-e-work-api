package com.malgn.domain.user.repository.query.impl;

import static com.malgn.domain.user.entity.QUserCompLeaveEntry.*;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.domain.user.entity.QUserCompLeaveEntry;
import com.malgn.domain.user.entity.UserCompLeaveEntry;
import com.malgn.domain.user.model.SearchUserCompLeaveEntryRequest;
import com.malgn.domain.user.model.v1.SearchUserCompLeaveEntryV1Request;
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

    @Override
    public List<UserCompLeaveEntry> getAll(String userUniqueId, SearchUserCompLeaveEntryRequest searchRequest) {
        return query.selectFrom(userCompLeaveEntry)
            .where(
                eqUserUniqueId(userUniqueId),
                mappingCondition(searchRequest))
            .orderBy(userCompLeaveEntry.effectiveDate.asc())
            .fetch();
    }

    /*
     * mapping condition
     */
    private Predicate mappingCondition(SearchUserCompLeaveEntryRequest searchRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchRequest.getClass().isAssignableFrom(SearchUserCompLeaveEntryV1Request.class)) {
            // v1
            SearchUserCompLeaveEntryV1Request searchV1Request = (SearchUserCompLeaveEntryV1Request)searchRequest;

            builder.and(eqIsAvailable(searchV1Request.isAvailable()));
        }

        return builder;
    }

    private BooleanExpression eqUserUniqueId(String userUniqueId) {
        return StringUtils.isNotBlank(userUniqueId) ? userCompLeaveEntry.userUniqueId.eq(userUniqueId) : null;
    }

    private BooleanExpression eqIsAvailable(boolean isAvailable) {
        return isAvailable ? userCompLeaveEntry.leaveDays.subtract(userCompLeaveEntry.usedDays).gt(0) : null;
    }
}
