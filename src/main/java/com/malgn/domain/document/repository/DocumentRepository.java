package com.malgn.domain.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.repository.query.DocumentQueryRepository;

public interface DocumentRepository extends JpaRepository<Document, Long>, DocumentQueryRepository {
}
