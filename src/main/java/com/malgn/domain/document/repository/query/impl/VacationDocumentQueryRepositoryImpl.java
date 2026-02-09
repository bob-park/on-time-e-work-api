package com.malgn.domain.document.repository.query.impl;

import static com.malgn.domain.document.entity.QVacationDocument.*;
import static com.malgn.domain.user.entity.QUserCompLeaveEntry.*;
import static com.malgn.domain.user.entity.QUserVacationUsedCompLeave.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import org.apache.commons.lang.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.common.model.Id;
import com.malgn.common.querydsl.model.QueryDslPath;
import com.malgn.common.querydsl.utils.QueryRepositoryUtils;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.VacationType;
import com.malgn.domain.document.model.SearchVacationDocumentRequest;
import com.malgn.domain.document.model.v1.SearchVacationDocumentV1Request;
import com.malgn.domain.document.repository.query.VacationDocumentQueryRepository;

@RequiredArgsConstructor
public class VacationDocumentQueryRepositoryImpl implements VacationDocumentQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<VacationDocument> search(SearchVacationDocumentRequest searchRequest, Pageable pageable) {

        List<VacationDocument> content =
            query.selectFrom(vacationDocument)
                .where(mappingCondition(searchRequest))
                .orderBy(sort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery =
            query.select(vacationDocument.id.count())
                .from(vacationDocument)
                .where(mappingCondition(searchRequest));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<VacationDocument> getDocumentById(Id<? extends Document, Long> id) {
        return Optional.ofNullable(
            query.selectFrom(vacationDocument)
                .leftJoin(vacationDocument.usedCompLeaves, userVacationUsedCompLeave).fetchJoin()
                .leftJoin(userVacationUsedCompLeave.compLeaveEntry, userCompLeaveEntry).fetchJoin()
                .where(vacationDocument.id.eq(id.getValue()))
                .fetchOne());
    }

    /*
     * mapping condition
     */
    private Predicate mappingCondition(SearchVacationDocumentRequest searchRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        // v1
        if (searchRequest.getClass().isAssignableFrom(SearchVacationDocumentV1Request.class)) {
            SearchVacationDocumentV1Request searchV1Request = (SearchVacationDocumentV1Request)searchRequest;

            builder.and(eqUserUniqueId(searchV1Request.userUniqueId()))
                .and(eqStatus(searchV1Request.status()))
                .and(eqVacationType(searchV1Request.vacationType()))
                .and(goeStartDateFrom(searchV1Request.startDateFrom()))
                .and(loeEndDateTo(searchV1Request.endDateTo()))
                .and(eqUserUniqueIds(searchV1Request.userUniqueIds()));

        }

        return builder;
    }

    private BooleanExpression eqUserUniqueId(String id) {
        return StringUtils.isNotBlank(id) ? vacationDocument.userUniqueId.eq(id) : null;
    }

    private BooleanExpression eqUserUniqueIds(List<String> ids) {
        return !ids.isEmpty() ? vacationDocument.userUniqueId.in(ids) : null;
    }

    private BooleanExpression eqStatus(DocumentStatus status) {
        return status != null ? vacationDocument.status.eq(status) : null;
    }

    private BooleanExpression eqVacationType(VacationType vacationType) {
        return vacationType != null ? vacationDocument.vacationType.eq(vacationType) : null;
    }

    private BooleanExpression goeStartDateFrom(LocalDate fromDate) {
        return fromDate != null ? vacationDocument.startDate.goe(fromDate) : null;
    }

    private BooleanExpression loeEndDateTo(LocalDate toDate) {
        return toDate != null ? vacationDocument.endDate.loe(toDate) : null;
    }

    /*
     * order
     */
    private OrderSpecifier<?>[] sort(Pageable pageable) {
        return QueryRepositoryUtils.sort(
            pageable,
            List.of(
                new QueryDslPath<>("vacationType", vacationDocument.vacationType),
                new QueryDslPath<>("startDate", vacationDocument.startDate),
                new QueryDslPath<>("status", vacationDocument.status),
                new QueryDslPath<>("createdDate", vacationDocument.createdDate)));
    }
}
