package com.malgn.domain.notification.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.OvertimeWorkDocument;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.repository.OvertimeWorkDocumentRepository;
import com.malgn.domain.user.feign.UserFeignClient;
import com.malgn.domain.user.model.UserResponse;
import com.malgn.notification.client.NotificationClient;
import com.malgn.notification.model.SendNotificationMessageRequest;

@Slf4j
@RequiredArgsConstructor
public class OvertimeWorkDocumentNotificationSender implements DocumentNotificationSender {

    private final NotificationClient notiClient;
    private final UserFeignClient userClient;

    private final OvertimeWorkDocumentRepository documentRepository;

    @Override
    public void send(String receiveUserUniqueId, Id<Document, Long> documentId) {
        OvertimeWorkDocument document =
            documentRepository.findDocument(documentId)
                .orElseThrow(() -> new NotFoundException(documentId));

        SendNotificationMessageRequest message =
            SendNotificationMessageRequest.builder()
                .displayMessage(parseDisplayMessage(document))
                .build();

        notiClient.sendUserNotification(receiveUserUniqueId, message);

        log.debug("sent notification message...");

    }

    @Override
    public boolean isSupport(DocumentType type) {
        return type == DocumentType.OVERTIME_WORK;
    }

    private String parseDisplayMessage(Document document) {

        StringBuilder builder = new StringBuilder();

        UserResponse user = userClient.getById(document.getUserUniqueId());

        builder.append(user.team().name())
            .append(" ")
            .append(user.username())
            .append(" ")
            .append(user.position().name())
            .append(" 이(가) 휴일근무보고서 신청하였습니다.");

        return builder.toString();
    }
}
