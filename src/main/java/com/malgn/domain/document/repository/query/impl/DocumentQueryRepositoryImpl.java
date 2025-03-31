package com.malgn.domain.document.repository.query.impl;

import static com.malgn.domain.document.entity.QDocument.*;

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
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.model.SearchDocumentRequest;
import com.malgn.domain.document.model.v1.SearchDocumentV1Request;
import com.malgn.domain.document.repository.query.DocumentQueryRepository;

@RequiredArgsConstructor
public class DocumentQueryRepositoryImpl implements DocumentQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<Document> search(SearchDocumentRequest searchRequest, Pageable pageable) {

        List<Document> content =
            query.selectFrom(document)
                .where(mappingCondition(searchRequest))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(sort(pageable))
                .fetch();

        JPAQuery<Long> countQuery =
            query.select(document.id.count())
                .from(document)
                .where(mappingCondition(searchRequest));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private Predicate mappingCondition(SearchDocumentRequest searchRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchRequest.getClass().isAssignableFrom(SearchDocumentV1Request.class)) {

            SearchDocumentV1Request searchV1Request = (SearchDocumentV1Request)searchRequest;

            builder.and(eqUserUniqueId(searchV1Request.userUniqueId()))
                .and(eqType(searchV1Request.type()))
                .and(eqStatus(searchV1Request.status()));

        }

        return builder;
    }

    private BooleanExpression eqUserUniqueId(String userUniqueId) {
        return StringUtils.isNotBlank(userUniqueId) ? document.userUniqueId.eq(userUniqueId) : null;
    }

    private BooleanExpression eqStatus(DocumentStatus status) {
        return status != null ? document.status.eq(status) : null;
    }

    private BooleanExpression eqType(DocumentType type) {
        return type != null ? document.type.eq(type) : null;
    }

    /*
     * order
     */
    private OrderSpecifier<?>[] sort(Pageable pageable) {
        return QueryRepositoryUtils.sort(
            pageable,
            List.of(
                new QueryDslPath<>("type", document.type),
                new QueryDslPath<>("status", document.status),
                new QueryDslPath<>("createdDate", document.createdDate)));
    }

}
