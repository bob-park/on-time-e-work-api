package com.malgn.domain.document.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import com.malgn.cqrs.event.EventPayload;
import com.malgn.cqrs.event.EventType;

@ToString
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum DocumentEventType implements EventType {

    DOCUMENT_CREATED("documents", "DOCUMENT_CREATED", DocumentCreatedEventPayload.class),
    DOCUMENT_REQUESTED("documents", "DOCUMENT_REQUESTED", DocumentRequestedEventPayload.class),
    DOCUMENT_APPROVED("documents", "DOCUMENT_APPROVED", DocumentApprovedEventPayload.class),
    DOCUMENT_REJECTED("documents", "DOCUMENT_REJECTED", DocumentRejectedEventPayload.class);

    private final String topic;
    private final String type;
    private final Class<? extends EventPayload> payloadClass;

    public static class Topic {
        public static final String DOCUMENT = "documents";
    }
}
