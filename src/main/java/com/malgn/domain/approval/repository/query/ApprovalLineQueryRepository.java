package com.malgn.domain.approval.repository.query;

import java.util.List;
import java.util.Optional;

import com.malgn.domain.approval.entity.ApprovalLine;
import com.malgn.domain.document.entity.type.DocumentType;

public interface ApprovalLineQueryRepository {

    List<ApprovalLine> getLines(Long teamId);

    Optional<ApprovalLine> getFirstLine(Long teamId, DocumentType documentType);

    Optional<ApprovalLine> getNextLine(Long teamId, Long parentId, DocumentType documentType);
}
