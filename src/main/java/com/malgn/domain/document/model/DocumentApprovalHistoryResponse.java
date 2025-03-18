package com.malgn.domain.document.model;

import com.malgn.common.model.CommonResponse;
import com.malgn.domain.approval.model.ApprovalLineResponse;
import com.malgn.domain.document.entity.type.ApprovalStatus;

public interface DocumentApprovalHistoryResponse extends CommonResponse {

    Long id();

    DocumentResponse document();

    ApprovalLineResponse approvalLine();

    ApprovalStatus status();

    String reason();
}
