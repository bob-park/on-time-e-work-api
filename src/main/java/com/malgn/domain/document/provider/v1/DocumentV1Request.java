package com.malgn.domain.document.provider.v1;

import lombok.Builder;

import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.provider.DocumentRequest;

@Builder
public record DocumentV1Request(Long documentId,
                                Long teamId,
                                DocumentType documentType)
    implements DocumentRequest {
}
