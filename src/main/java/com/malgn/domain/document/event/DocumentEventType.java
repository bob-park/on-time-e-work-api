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

    CREATE_DOCUMENT("on-time-e-work-document", CreateDocumentEventPayload.class);

    private final String topic;
    private final Class<? extends EventPayload> payloadClass;


    public static class Topic {
        public static final String DOCUMENT = "on-time-e-work-document";
    }
}
