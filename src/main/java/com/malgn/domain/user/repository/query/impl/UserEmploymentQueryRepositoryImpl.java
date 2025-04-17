package com.malgn.domain.user.repository.query.impl;

import static com.malgn.domain.user.entity.QUserEmployment.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import org.apache.commons.lang3.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.common.querydsl.model.QueryDslPath;
import com.malgn.common.querydsl.utils.QueryRepositoryUtils;
import com.malgn.domain.user.entity.UserEmployment;
import com.malgn.domain.user.entity.type.EmployStatus;
import com.malgn.domain.user.model.SearchUserEmploymentRequest;
import com.malgn.domain.user.model.v1.SearchUserEmploymentV1Request;
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

    @Override
    public Page<UserEmployment> search(SearchUserEmploymentRequest request, Pageable pageable) {

        List<UserEmployment> content =
            query.selectFrom(userEmployment)
                .where(mappingCondition(request))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sort(pageable))
                .fetch();

        JPAQuery<Long> countQuery =
            query.select(userEmployment.count())
                .from(userEmployment)
                .where(mappingCondition(request));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    /*
     * mapping
     */
    private Predicate mappingCondition(SearchUserEmploymentRequest request) {

        BooleanBuilder builder = new BooleanBuilder();

        if (request.getClass().isAssignableFrom(SearchUserEmploymentV1Request.class)) {
            // v1
            SearchUserEmploymentV1Request searchV1Request = (SearchUserEmploymentV1Request)request;

            builder.and(eqUserUniqueId(searchV1Request.userUniqueId()))
                .and(goeEffectiveDateFrom(searchV1Request.effectiveDateFrom()))
                .and(loeEffectiveDateTo(searchV1Request.effectiveDateTo()))
                .and(eqStatus(searchV1Request.status()));
        }

        return builder;
    }

    private BooleanExpression eqUserUniqueId(String userUniqueId) {
        return StringUtils.isNotBlank(userUniqueId) ? userEmployment.userUniqueId.eq(userUniqueId) : null;
    }

    private BooleanExpression goeEffectiveDateFrom(LocalDateTime effectiveDateFrom) {
        return effectiveDateFrom != null ? userEmployment.effectiveDate.goe(effectiveDateFrom) : null;
    }

    private BooleanExpression loeEffectiveDateTo(LocalDateTime effectiveDateTo) {
        return effectiveDateTo != null ? userEmployment.effectiveDate.loe(effectiveDateTo) : null;
    }

    private BooleanExpression eqStatus(EmployStatus status) {
        return userEmployment.status.eq(status != null ? status : EmployStatus.ACTIVE);
    }

    /*
     * order
     */
    private OrderSpecifier<?>[] sort(Pageable pageable) {
        return QueryRepositoryUtils.sort(
            pageable,
            List.of(
                new QueryDslPath<>("effectiveDate", userEmployment.effectiveDate),
                new QueryDslPath<>("status", userEmployment.status),
                new QueryDslPath<>("createdDate", userEmployment.createdDate)));
    }

}
