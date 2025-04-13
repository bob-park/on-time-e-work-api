package com.malgn.domain.notification.sender;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.type.DocumentType;

public interface DocumentNotificationSender {

    void send(String receiveUserUniqueId, Id<Document, Long> documentId);

    default boolean isSupport(DocumentType type) {
        return false;
    }
}
