package com.malgn.domain.notification.sender;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.type.DocumentType;

@Slf4j
public class DelegatingNotificationSender {

    private final List<DocumentNotificationSender> senders = new ArrayList<>();

    public void send(String receiveUserUniqueId, Id<Document, Long> documentId, DocumentType type) {
        for (DocumentNotificationSender sender : senders) {
            if (sender.isSupport(type)) {
                sender.send(receiveUserUniqueId, documentId);
            }
        }
    }

    public void addSender(DocumentNotificationSender sender) {
        senders.add(sender);

        log.debug("added sender: {}", sender.getClass().getSimpleName());
    }

}
