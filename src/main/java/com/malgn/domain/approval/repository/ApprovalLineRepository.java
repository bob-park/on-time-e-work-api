package com.malgn.domain.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.approval.entity.ApprovalLine;
import com.malgn.domain.approval.repository.query.ApprovalLineQueryRepository;

public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long>, ApprovalLineQueryRepository {
}
