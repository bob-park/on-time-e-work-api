package com.malgn.domain.document.repository.query.impl;

import static com.malgn.domain.approval.entity.QApprovalLine.*;
import static com.malgn.domain.document.entity.QDocument.*;
import static com.malgn.domain.document.entity.QDocumentApprovalHistory.*;
import static com.malgn.domain.document.entity.QVacationDocument.*;

import java.time.LocalDate;
import java.time.LocalTime;
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
import com.malgn.domain.approval.entity.QApprovalLine;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.entity.QDocument;
import com.malgn.domain.document.entity.QDocumentApprovalHistory;
import com.malgn.domain.document.entity.type.ApprovalStatus;
import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.model.RejectDocumentRequest;
import com.malgn.domain.document.model.SearchDocumentApprovalHistoryRequest;
import com.malgn.domain.document.model.v1.SearchDocumentApprovalHistoryV1Request;
import com.malgn.domain.document.repository.query.DocumentApprovalHistoryQueryRepository;

@RequiredArgsConstructor
public class DocumentApprovalHistoryQueryRepositoryImpl implements DocumentApprovalHistoryQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<DocumentApprovalHistory> getHistory(Id<DocumentApprovalHistory, Long> id) {
        return Optional.ofNullable(
            query.selectFrom(documentApprovalHistory)
                .join(documentApprovalHistory.document, document).fetchJoin()
                .join(documentApprovalHistory.approvalLine, approvalLine).fetchJoin()
                .where(documentApprovalHistory.id.eq(id.getValue()))
                .fetchOne());
    }

    @Override
    public Page<DocumentApprovalHistory> search(SearchDocumentApprovalHistoryRequest searchRequest, Pageable pageable) {

        List<DocumentApprovalHistory> content =
            query.selectFrom(documentApprovalHistory)
                .join(documentApprovalHistory.document, document).fetchJoin()
                .where(mappingCondition(searchRequest))
                .orderBy(sort(pageable))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery =
            query.select(documentApprovalHistory.id.count())
                .from(documentApprovalHistory)
                .join(documentApprovalHistory.document, document)
                .where(mappingCondition(searchRequest));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private Predicate mappingCondition(SearchDocumentApprovalHistoryRequest searchRequest) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchRequest.getClass().isAssignableFrom(SearchDocumentApprovalHistoryV1Request.class)) {
            SearchDocumentApprovalHistoryV1Request searchV1Request = (SearchDocumentApprovalHistoryV1Request)searchRequest;

            builder.and(eqUserUniqueId(searchV1Request.userUniqueId()))
                .and(eqStatus(searchV1Request.status()))
                .and(goeCreatedDateFrom(searchV1Request.createdDateFrom()))
                .and(loeCreatedDateTo(searchV1Request.createdDateTo()));
        }

        return builder;
    }

    private BooleanExpression eqUserUniqueId(String userUniqueId) {
        return StringUtils.isNotBlank(userUniqueId) ? approvalLine.userUniqueId.eq(userUniqueId) : null;
    }

    private BooleanExpression eqStatus(ApprovalStatus status) {
        return status != null ? documentApprovalHistory.status.eq(status) : null;
    }

    private BooleanExpression goeCreatedDateFrom(LocalDate fromDate) {
        return fromDate != null ? documentApprovalHistory.createdDate.goe(fromDate.atStartOfDay()) : null;
    }

    private BooleanExpression loeCreatedDateTo(LocalDate toDate) {
        return toDate != null ? documentApprovalHistory.createdDate.loe(toDate.atTime(LocalTime.MAX)) : null;
    }

    /*
     * order
     */
    private OrderSpecifier<?>[] sort(Pageable pageable) {
        return QueryRepositoryUtils.sort(
            pageable,
            List.of(
                new QueryDslPath<>("createdDate", documentApprovalHistory.createdDate),
                new QueryDslPath<>("status", documentApprovalHistory.status)));
    }
}
