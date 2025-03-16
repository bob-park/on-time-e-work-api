package com.malgn.domain.document.model;

import com.malgn.common.model.CommonResponse;
import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;

public interface DocumentResponse extends CommonResponse {

    Long id();

    DocumentType type();

    DocumentStatus status();

    String writerId();
}
