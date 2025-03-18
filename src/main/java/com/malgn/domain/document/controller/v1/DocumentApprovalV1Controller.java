package com.malgn.domain.document.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.model.ApproveDocumentRequest;
import com.malgn.domain.document.model.DocumentApprovalHistoryResponse;
import com.malgn.domain.document.model.v1.RejectDocumentV1Request;
import com.malgn.domain.document.service.v1.DocumentApprovalHistoryV1Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/documents/approval")
public class DocumentApprovalV1Controller {

    private final DocumentApprovalHistoryV1Service approvalHistoryService;

    @PostMapping(path = "{id:\\d+}")
    public DocumentApprovalHistoryResponse approve(@PathVariable long id) {
        return approvalHistoryService.approve(Id.of(DocumentApprovalHistory.class, id), new ApproveDocumentRequest() {
        });
    }

    @PostMapping(path = "{id:\\d+}/reject")
    public DocumentApprovalHistoryResponse reject(@PathVariable long id,
        @RequestBody RejectDocumentV1Request rejectRequest) {
        return approvalHistoryService.reject(Id.of(DocumentApprovalHistory.class, id), rejectRequest);
    }

}
