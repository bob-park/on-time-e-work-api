package com.malgn.domain.document.model;

import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;

public interface SearchDocumentRequest {

    String userUniqueId();

    DocumentStatus status();

    DocumentType type();
}
