package com.malgn.domain.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.repository.query.VacationDocumentQueryRepository;

public interface VacationDocumentRepository extends JpaRepository<VacationDocument, Long>,
    VacationDocumentQueryRepository {
}
