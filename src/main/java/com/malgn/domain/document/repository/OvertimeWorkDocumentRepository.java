package com.malgn.domain.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.document.entity.OvertimeWorkDocument;
import com.malgn.domain.document.repository.query.OvertimeWorkDocumentQueryRepository;

public interface OvertimeWorkDocumentRepository extends JpaRepository<OvertimeWorkDocument, Long>,
    OvertimeWorkDocumentQueryRepository {
}
