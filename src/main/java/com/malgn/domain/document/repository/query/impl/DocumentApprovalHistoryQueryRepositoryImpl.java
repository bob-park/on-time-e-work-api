package com.malgn.domain.document.repository.query.impl;

import static com.malgn.domain.approval.entity.QApprovalLine.*;
import static com.malgn.domain.document.entity.QDocument.*;
import static com.malgn.domain.document.entity.QDocumentApprovalHistory.*;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.common.model.Id;
import com.malgn.domain.approval.entity.QApprovalLine;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.entity.QDocument;
import com.malgn.domain.document.entity.QDocumentApprovalHistory;
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
}
