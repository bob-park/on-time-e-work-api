package com.malgn.domain.document.model.v1;

import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.model.SearchDocumentRequest;

public record SearchDocumentV1Request(String userUniqueId,
                                      DocumentStatus status,
                                      DocumentType type)
implements SearchDocumentRequest {
}
