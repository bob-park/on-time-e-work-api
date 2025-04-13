package com.malgn.domain.notification.sender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.Lists;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.model.Id;
import com.malgn.common.utils.date.DateUtils;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.entity.type.VacationSubType;
import com.malgn.domain.document.entity.type.VacationType;
import com.malgn.domain.document.repository.VacationDocumentRepository;
import com.malgn.domain.user.feign.UserFeignClient;
import com.malgn.domain.user.model.UserResponse;
import com.malgn.notification.client.NotificationClient;
import com.malgn.notification.model.SendNotificationMessageBlockRequest;
import com.malgn.notification.model.SendNotificationMessageRequest;

@Slf4j
@RequiredArgsConstructor
public class VacationDocumentNotificationSender implements DocumentNotificationSender {

    private static final DocumentType DOCUMENT_TYPE = DocumentType.VACATION;
    private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final NotificationClient notiClient;
    private final UserFeignClient userClient;

    private final VacationDocumentRepository vacationDocumentRepository;

    @Override
    public void send(String receiveUserUniqueId, Id<Document, Long> documentId) {

        VacationDocument document =
            vacationDocumentRepository.getDocumentById(documentId)
                .orElseThrow(() -> new NotFoundException(documentId));

        SendNotificationMessageRequest message =
            SendNotificationMessageRequest.builder()
                .displayMessage(parseDisplayMessage(document))
                .fields(parseBlocks(document))
                .build();

        notiClient.sendUserNotification(receiveUserUniqueId, message);

        log.debug("sent notification message...");

    }

    @Override
    public boolean isSupport(DocumentType type) {
        return type == DOCUMENT_TYPE;
    }

    private String parseDisplayMessage(VacationDocument document) {

        StringBuilder builder = new StringBuilder();

        UserResponse user = userClient.getById(document.getUserUniqueId());

        builder.append(user.team().name())
            .append(" ")
            .append(user.username())
            .append(" ")
            .append(user.position().name())
            .append(" 이(가) 휴가 신청하였습니다.");

        return builder.toString();
    }

    private List<SendNotificationMessageBlockRequest> parseBlocks(VacationDocument document) {
        List<SendNotificationMessageBlockRequest> blocks = Lists.newArrayList();

        blocks.add(
            SendNotificationMessageBlockRequest.builder()
                .field("휴가 신청 종류")
                .text(parseType(document.getVacationType(), document.getVacationSubType()))
                .build());

        blocks.add(
            SendNotificationMessageBlockRequest.builder()
                .field("휴가 신청 일수")
                .text(document.getUsedDays().toString())
                .build());

        blocks.add(
            SendNotificationMessageBlockRequest.builder()
                .field("휴가 신청 시작일")
                .text(parseMessageDate(document.getStartDate()))
                .build());

        blocks.add(
            SendNotificationMessageBlockRequest.builder()
                .field("휴가 신청 종료일")
                .text(parseMessageDate(document.getEndDate()))
                .build());

        return blocks;
    }

    private String parseType(VacationType type, VacationSubType subType) {

        String result = "";

        switch (type) {
            case OFFICIAL -> result = "공가";
            case COMPENSATORY -> result = "보상 휴가";
            case GENERAL -> result = "연차";
            default -> {
                return "이건 뭘까?";
            }
        }

        if (subType != null) {
            switch (subType) {
                case AM_HALF_DAY_OFF -> result += " - 오전 반차";
                case PM_HALF_DAY_OFF -> result += " - 오후 반차";
                default -> {
                    // ignore
                }
            }
        }

        return result;
    }

    private String parseMessageDate(LocalDate date) {
        return date.format(DEFAULT_DATE_TIME_FORMATTER) + "(" + DateUtils.getDayOfWeek(date.getDayOfWeek().getValue())
            + ")";
    }
}
