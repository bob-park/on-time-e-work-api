package com.malgn.domain.document.repository.query.impl;

import static com.malgn.domain.document.entity.QOvertimeWorkDocument.*;
import static com.malgn.domain.document.entity.QOvertimeWorkTime.*;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.OvertimeWorkDocument;
import com.malgn.domain.document.entity.QOvertimeWorkDocument;
import com.malgn.domain.document.entity.QOvertimeWorkTime;
import com.malgn.domain.document.repository.query.OvertimeWorkDocumentQueryRepository;

@RequiredArgsConstructor
public class OvertimeWorkDocumentQueryRepositoryImpl implements OvertimeWorkDocumentQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<OvertimeWorkDocument> findDocument(Id<? extends Document, Long> id) {
        return Optional.ofNullable(
            query.selectFrom(overtimeWorkDocument)
                .where(overtimeWorkDocument.id.eq(id.getValue()))
                .fetchOne());
    }
}
