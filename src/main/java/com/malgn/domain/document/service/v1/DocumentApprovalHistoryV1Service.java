package com.malgn.domain.document.service.v1;

import static com.google.common.base.Preconditions.*;
import static com.malgn.domain.document.model.v1.DocumentApprovalHistoryV1Response.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.ServiceRuntimeException;
import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.model.ApproveDocumentRequest;
import com.malgn.domain.document.model.DocumentApprovalHistoryResponse;
import com.malgn.domain.document.model.RejectDocumentRequest;
import com.malgn.domain.document.model.SearchDocumentApprovalHistoryRequest;
import com.malgn.domain.document.model.v1.DocumentApprovalHistoryV1Response;
import com.malgn.domain.document.model.v1.RejectDocumentV1Request;
import com.malgn.domain.document.processor.DelegatingApprovalProcessor;
import com.malgn.domain.document.repository.DocumentApprovalHistoryRepository;
import com.malgn.domain.document.service.DocumentApprovalHistoryService;
import com.malgn.domain.user.exception.OverLeaveEntryException;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DocumentApprovalHistoryV1Service implements DocumentApprovalHistoryService {

    private final DelegatingApprovalProcessor processor;

    private final DocumentApprovalHistoryRepository documentApprovalHistoryRepository;

    @Override
    public Page<DocumentApprovalHistoryResponse> search(SearchDocumentApprovalHistoryRequest searchRequest,
        Pageable pageable) {

        Page<DocumentApprovalHistory> result = documentApprovalHistoryRepository.search(searchRequest, pageable);

        return result.map(item -> DocumentApprovalHistoryV1Response.from(item, true));
    }

    @Transactional(noRollbackFor = {OverLeaveEntryException.class, ServiceRuntimeException.class})
    @Override
    public DocumentApprovalHistoryResponse approve(Id<DocumentApprovalHistory, Long> id,
        ApproveDocumentRequest approveRequest) {

        DocumentApprovalHistory history =
            documentApprovalHistoryRepository.getHistory(id)
                .orElseThrow(() -> new NotFoundException(id));

        processor.approval(id, history.getDocument().getType());

        return from(history, true);
    }

    @Transactional
    @Override
    public DocumentApprovalHistoryResponse reject(Id<DocumentApprovalHistory, Long> id,
        RejectDocumentRequest rejectRequest) {

        RejectDocumentV1Request rejectV1Request = (RejectDocumentV1Request)rejectRequest;

        checkArgument(StringUtils.isNotBlank(rejectV1Request.reason()), "reason must be provided.");

        DocumentApprovalHistory history =
            documentApprovalHistoryRepository.getHistory(id)
                .orElseThrow(() -> new NotFoundException(id));

        processor.reject(id, rejectV1Request.reason(), history.getDocument().getType());

        return from(history, true);
    }
}
