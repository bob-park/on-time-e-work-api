package com.malgn.domain.document.provider;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.type.DocumentType;

public interface CancelDocumentProvider {

    void cancel(Id<Document, Long> documentId);

    default boolean isSupport(DocumentType type) {
        return false;
    }

}
