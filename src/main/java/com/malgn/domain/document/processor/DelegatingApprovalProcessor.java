package com.malgn.domain.document.processor;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.model.Id;
import com.malgn.domain.approval.entity.ApprovalLine;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.repository.DocumentApprovalHistoryRepository;
import com.malgn.domain.notification.sender.DelegatingNotificationSender;

@Slf4j
@RequiredArgsConstructor
public class DelegatingApprovalProcessor {

    private final List<ApprovalProcessor> processors = new ArrayList<>();

    private final DocumentApprovalHistoryRepository approvalHistoryRepository;
    private final DelegatingNotificationSender notificationSender;

    public void add(ApprovalProcessor processor) {
        processors.add(processor);

        log.debug("added approval processor {}", processor.getClass().getSimpleName());
    }

    public DocumentApprovalHistory approval(Id<DocumentApprovalHistory, Long> id, DocumentType documentType) {

        // 공통 처리
        DocumentApprovalHistory history =
            approvalHistoryRepository.getHistory(id)
                .orElseThrow(() -> new NotFoundException(id));

        Document document = history.getDocument();

        ApprovalLine nextLine = history.getApprovalLine().getNext();

        if (nextLine != null) {

            // 다음 결재라인 이 있는 경우 생성
            DocumentApprovalHistory createdHistory =
                DocumentApprovalHistory.builder()
                    .build();

            document.addApprovalHistory(createdHistory);
            nextLine.addApprovalHistory(createdHistory);

            createdHistory = approvalHistoryRepository.save(createdHistory);

            log.debug("created next approval history. ({})", createdHistory);

            try {
                notificationSender.send(
                    nextLine.getUserUniqueId(),
                    Id.of(Document.class, document.getId()),
                    document.getType());
            } catch (Exception e) {
                log.error("Failed send message - {}", e.getMessage(), e);
            }

        } else {
            // 마지막 승인인 경우 처리
            for (ApprovalProcessor processor : processors) {
                if (processor.isSupport(documentType)) {
                    processor.approve(id);
                }
            }
        }

        history.approve();

        log.debug("approved history. ({})", history);

        return history;
    }

    public void reject(Id<DocumentApprovalHistory, Long> id, String reason,
        DocumentType documentType) {

        for (ApprovalProcessor processor : processors) {
            if (processor.isSupport(documentType)) {
                processor.reject(id, reason);
            }
        }

    }

}
