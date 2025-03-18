package com.malgn.domain.document.processor.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.model.Id;
import com.malgn.domain.approval.entity.ApprovalLine;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.processor.ApprovalProcessor;
import com.malgn.domain.document.repository.DocumentApprovalHistoryRepository;
import com.malgn.domain.user.repository.UserCompLeaveEntryRepository;
import com.malgn.domain.user.repository.UserLeaveEntryRepository;

@Slf4j
@RequiredArgsConstructor
public class VacationApprovalV1Processor implements ApprovalProcessor {

    private final DocumentApprovalHistoryRepository approvalHistoryRepository;
    private final UserLeaveEntryRepository userLeaveEntryRepository;
    private final UserCompLeaveEntryRepository userCompLeaveEntryRepository;

    @Override
    public boolean isSupport(DocumentType documentType) {
        return documentType == DocumentType.VACATION;
    }

    @Override
    public DocumentApprovalHistory approve(Id<DocumentApprovalHistory, Long> id) {
        DocumentApprovalHistory history =
            approvalHistoryRepository.getHistory(id)
                .orElseThrow(() -> new NotFoundException(id));

        Document document = history.getDocument();

        history.approve();

        log.debug("approved history. ({})", history);

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

        } else {
            // 최종 승인자인 경우 최종 승인 처리
            document.approve();

            // 최종 승인인 경우 휴가 감소 처리
            log.debug("final approval document... ({})", document);
        }

        return history;
    }

    @Override
    public DocumentApprovalHistory reject(Id<DocumentApprovalHistory, Long> id, String reason) {
        DocumentApprovalHistory history =
            approvalHistoryRepository.getHistory(id)
                .orElseThrow(() -> new NotFoundException(id));

        Document document = history.getDocument();

        history.reject(reason);
        document.reject();

        log.debug("rejected history. ({})", history);

        return history;
    }
}
