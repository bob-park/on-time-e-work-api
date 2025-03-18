package com.malgn.domain.document.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.model.ApproveDocumentRequest;
import com.malgn.domain.document.model.DocumentApprovalHistoryResponse;
import com.malgn.domain.document.model.RejectDocumentRequest;
import com.malgn.domain.document.model.SearchDocumentApprovalHistoryRequest;

public interface DocumentApprovalHistoryService {

    Page<DocumentApprovalHistoryResponse> search(SearchDocumentApprovalHistoryRequest searchRequest, Pageable pageable);

    DocumentApprovalHistoryResponse approve(Id<DocumentApprovalHistory, Long> id,
        ApproveDocumentRequest approveRequest);

    DocumentApprovalHistoryResponse reject(Id<DocumentApprovalHistory, Long> id, RejectDocumentRequest rejectRequest);
}

