package com.malgn.domain.document.model.v1;

import java.time.LocalDateTime;

import lombok.Builder;

import com.malgn.domain.approval.model.ApprovalLineResponse;
import com.malgn.domain.approval.model.v1.ApprovalLineV1Response;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.entity.type.ApprovalStatus;
import com.malgn.domain.document.model.DocumentApprovalHistoryResponse;
import com.malgn.domain.document.model.DocumentResponse;

@Builder
public record DocumentApprovalHistoryV1Response(Long id,
                                                DocumentResponse document,
                                                ApprovalLineResponse approvalLine,
                                                ApprovalStatus status,
                                                String reason,
                                                LocalDateTime createdDate,
                                                String createdBy,
                                                LocalDateTime lastModifiedDate,
                                                String lastModifiedBy)
    implements DocumentApprovalHistoryResponse {

    public static DocumentApprovalHistoryResponse from(DocumentApprovalHistory entity) {
        return from(entity, false);
    }

    public static DocumentApprovalHistoryResponse from(DocumentApprovalHistory entity, boolean detail) {
        return DocumentApprovalHistoryV1Response.builder()
            .id(entity.getId())
            .document(detail ? DocumentV1Response.from(entity.getDocument()) : null)
            .approvalLine(ApprovalLineV1Response.from(entity.getApprovalLine()))
            .status(entity.getStatus())
            .reason(entity.getReason())
            .createdDate(entity.getCreatedDate())
            .createdBy(entity.getCreatedBy())
            .lastModifiedDate(entity.getLastModifiedDate())
            .lastModifiedBy(entity.getLastModifiedBy())
            .build();
    }
}
