package com.malgn.domain.document.model;

import java.util.List;

import com.malgn.common.model.CommonResponse;
import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;

public interface DocumentResponse extends CommonResponse {

    Long id();

    DocumentType type();

    DocumentStatus status();

    String userUniqueId();

    default List<DocumentApprovalHistoryResponse> approvalHistories() {
        return List.of();
    }
}
