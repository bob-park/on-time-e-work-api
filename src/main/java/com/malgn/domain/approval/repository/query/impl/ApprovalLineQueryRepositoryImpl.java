package com.malgn.domain.approval.repository.query.impl;

import static com.malgn.domain.approval.entity.QApprovalLine.*;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.domain.approval.entity.ApprovalLine;
import com.malgn.domain.approval.entity.QApprovalLine;
import com.malgn.domain.approval.repository.query.ApprovalLineQueryRepository;
import com.malgn.domain.document.entity.type.DocumentType;

@RequiredArgsConstructor
public class ApprovalLineQueryRepositoryImpl implements ApprovalLineQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<ApprovalLine> getLines(Long teamId) {

        QApprovalLine parent = new QApprovalLine("parent");

        return query.selectFrom(approvalLine)
            .leftJoin(approvalLine.parent, parent).fetchJoin()
            .where(approvalLine.teamId.eq(teamId))
            .fetch();
    }

    @Override
    public Optional<ApprovalLine> getFirstLine(Long teamId, DocumentType documentType) {
        return Optional.ofNullable(
            query.selectFrom(approvalLine)
                .where(
                    approvalLine.parent.isNull(),
                    approvalLine.teamId.eq(teamId),
                    approvalLine.documentType.eq(documentType))
                .fetchOne());
    }

    @Override
    public Optional<ApprovalLine> getNextLine(Long teamId, Long parentId, DocumentType documentType) {

        QApprovalLine parent = new QApprovalLine("parent");

        return Optional.ofNullable(
            query.selectFrom(approvalLine)
                .leftJoin(approvalLine.parent, parent).fetchJoin()
                .where(
                    approvalLine.teamId.eq(teamId),
                    parent.id.eq(parentId),
                    approvalLine.documentType.eq(documentType))
                .fetchOne());
    }
}
