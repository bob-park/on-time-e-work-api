package com.malgn.domain.approval.model.v1;

import com.malgn.domain.approval.model.SearchApprovalLineRequest;
import com.malgn.domain.document.entity.type.DocumentType;

public record SearchApprovalLineV1Request(Long teamId,
                                          DocumentType documentType)
    implements SearchApprovalLineRequest {
}
