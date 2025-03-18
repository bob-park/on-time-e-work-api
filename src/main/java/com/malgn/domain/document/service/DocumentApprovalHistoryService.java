package com.malgn.domain.document.service;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.model.ApproveDocumentRequest;
import com.malgn.domain.document.model.DocumentApprovalHistoryResponse;
import com.malgn.domain.document.model.RejectDocumentRequest;

public interface DocumentApprovalHistoryService {

    DocumentApprovalHistoryResponse approve(Id<DocumentApprovalHistory, Long> id,
        ApproveDocumentRequest approveRequest);

    DocumentApprovalHistoryResponse reject(Id<DocumentApprovalHistory, Long> id, RejectDocumentRequest rejectRequest);
}

