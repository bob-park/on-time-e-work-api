package com.malgn.domain.document.processor;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.entity.type.DocumentType;

public interface ApprovalProcessor {

    default boolean isSupport(DocumentType documentType) {
        return false;
    }

    DocumentApprovalHistory approve(Id<DocumentApprovalHistory, Long> id);

    DocumentApprovalHistory reject(Id<DocumentApprovalHistory, Long> id, String reason);
}
