package com.malgn.domain.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.document.entity.DocumentApprovalHistory;

public interface DocumentApprovalHistoryRepository extends JpaRepository<DocumentApprovalHistory, Long> {
}
