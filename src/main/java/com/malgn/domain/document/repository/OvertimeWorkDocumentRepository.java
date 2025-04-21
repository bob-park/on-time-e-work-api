package com.malgn.domain.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.document.entity.OvertimeWorkDocument;

public interface OvertimeWorkDocumentRepository extends JpaRepository<OvertimeWorkDocument, Long> {
}
