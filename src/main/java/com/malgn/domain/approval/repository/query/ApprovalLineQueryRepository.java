package com.malgn.domain.approval.repository.query;

import java.util.List;
import java.util.Optional;

import com.malgn.domain.approval.entity.ApprovalLine;

public interface ApprovalLineQueryRepository {

    List<ApprovalLine> getLines(Long teamId);

    Optional<ApprovalLine> getFirstLine(Long teamId);
}
