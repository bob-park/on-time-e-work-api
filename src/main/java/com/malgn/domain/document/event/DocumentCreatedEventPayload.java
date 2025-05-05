package com.malgn.domain.document.event;

import lombok.Builder;

import com.malgn.cqrs.event.EventPayload;
import com.malgn.domain.document.entity.type.DocumentType;

@Builder
public record DocumentCreatedEventPayload(Long id,
                                          DocumentType type,
                                          String userUniqueId)
    implements EventPayload {
}
