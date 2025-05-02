package com.malgn.domain.document.command.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.malgn.cqrs.event.Event;
import com.malgn.cqrs.event.handler.CommandHandler;
import com.malgn.domain.document.event.CreateDocumentEventPayload;
import com.malgn.domain.document.event.DocumentEventType;

@Slf4j
@RequiredArgsConstructor
public class DocumentCommandHandler implements CommandHandler<CreateDocumentEventPayload> {

    @Override
    public void handle(Event<CreateDocumentEventPayload> event) {
        log.debug("handling event. ({})", event);
    }

    @Override
    public boolean supports(Event<CreateDocumentEventPayload> event) {
        return event.getType() == DocumentEventType.CREATE_DOCUMENT;
    }
}
