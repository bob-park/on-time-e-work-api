package com.malgn.domain.document.model.v1;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

import com.malgn.domain.document.entity.OvertimeWorkDocument;
import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.model.DocumentApprovalHistoryResponse;
import com.malgn.domain.document.model.OvertimeWorkDocumentResponse;
import com.malgn.domain.document.model.OvertimeWorkTimeResponse;

@Builder
public record OvertimeWorkDocumentV1Response(Long id,
                                             DocumentType type,
                                             DocumentStatus status,
                                             String userUniqueId,
                                             List<DocumentApprovalHistoryResponse> approvalHistories,
                                             LocalDateTime createdDate,
                                             String createdBy,
                                             LocalDateTime lastModifiedDate,
                                             String lastModifiedBy,
                                             List<OvertimeWorkTimeResponse> workTimes)
    implements OvertimeWorkDocumentResponse {

    public static OvertimeWorkDocumentResponse from(OvertimeWorkDocument entity) {
        return OvertimeWorkDocumentV1Response.builder()
            .id(entity.getId())
            .type(entity.getType())
            .status(entity.getStatus())
            .userUniqueId(entity.getUserUniqueId())
            .approvalHistories(
                entity.getApprovalHistories().stream()
                    .map(DocumentApprovalHistoryV1Response::from)
                    .toList())
            .createdDate(entity.getCreatedDate())
            .createdBy(entity.getCreatedBy())
            .lastModifiedDate(entity.getLastModifiedDate())
            .lastModifiedBy(entity.getLastModifiedBy())
            .workTimes(
                entity.getTimes().stream()
                    .map(OvertimeWorkTimeV1Response::from)
                    .toList())
            .build();
    }
}
