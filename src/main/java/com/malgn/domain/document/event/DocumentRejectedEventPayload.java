package com.malgn.domain.document.event;

import lombok.Builder;

import com.malgn.cqrs.event.EventPayload;
import com.malgn.domain.document.entity.type.DocumentType;

@Builder
public record DocumentRejectedEventPayload(Long id,
                                           DocumentType type,
                                           String userUniqueId,
                                           String receiveUserUniqueId)
    implements EventPayload {
}
