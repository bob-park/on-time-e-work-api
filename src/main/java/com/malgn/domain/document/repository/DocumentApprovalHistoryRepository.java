package com.malgn.domain.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.repository.query.DocumentApprovalHistoryQueryRepository;

public interface DocumentApprovalHistoryRepository extends JpaRepository<DocumentApprovalHistory, Long>,
    DocumentApprovalHistoryQueryRepository {
}
