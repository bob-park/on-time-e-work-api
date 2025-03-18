package com.malgn.domain.approval.model;

import com.malgn.domain.document.entity.type.DocumentType;

public interface SearchApprovalLineRequest {

    Long teamId();

    DocumentType documentType();

}
