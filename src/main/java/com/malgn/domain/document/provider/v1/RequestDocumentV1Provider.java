package com.malgn.domain.document.provider.v1;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

import com.malgn.common.exception.NotFoundException;
import com.malgn.cqrs.outbox.publish.OutboxEventPublisher;
import com.malgn.domain.approval.entity.ApprovalLine;
import com.malgn.domain.approval.repository.ApprovalLineRepository;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.event.DocumentEventType;
import com.malgn.domain.document.event.DocumentRequestedEventPayload;
import com.malgn.domain.document.provider.DocumentRequest;
import com.malgn.domain.document.provider.RequestDocumentProvider;
import com.malgn.domain.document.repository.DocumentApprovalHistoryRepository;
import com.malgn.domain.document.repository.DocumentRepository;

@Slf4j
@RequiredArgsConstructor
public class RequestDocumentV1Provider implements RequestDocumentProvider {

    private final ApprovalLineRepository approvalLineRepository;
    private final DocumentRepository documentRepository;
    private final DocumentApprovalHistoryRepository historyRepository;

    private final OutboxEventPublisher publisher;

    @Transactional
    @Override
    public void request(DocumentRequest request) {

        DocumentV1Request requestV1 = (DocumentV1Request)request;

        checkArgument(isNotEmpty(requestV1.documentId()), "documentId must be provided.");
        checkArgument(isNotEmpty(requestV1.teamId()), "teamId must be provided.");
        checkArgument(isNotEmpty(requestV1.documentType()), "documentType must be provided.");

        Document document =
            documentRepository.findById(requestV1.documentId())
                .orElseThrow(() -> new NotFoundException(Document.class, requestV1.documentId()));

        document.request();

        ApprovalLine line =
            approvalLineRepository.getFirstLine(requestV1.teamId(), requestV1.documentType())
                .orElseThrow(() -> new NotFoundException(ApprovalLine.class, requestV1.teamId()));

        DocumentApprovalHistory createdHistory = DocumentApprovalHistory.builder().build();

        document.addApprovalHistory(createdHistory);
        line.addApprovalHistory(createdHistory);

        createdHistory = historyRepository.save(createdHistory);

        log.debug("created document approval history. ({})", createdHistory);

        publisher.publish(
            DocumentEventType.DOCUMENT_REQUESTED,
            DocumentRequestedEventPayload.builder()
                .id(document.getId())
                .type(document.getType())
                .userUniqueId(document.getUserUniqueId())
                .receiveUserUniqueId(line.getUserUniqueId())
                .build());
    }
}
