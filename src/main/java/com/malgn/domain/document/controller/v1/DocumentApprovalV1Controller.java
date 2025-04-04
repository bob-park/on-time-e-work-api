package com.malgn.domain.document.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.model.ApproveDocumentRequest;
import com.malgn.domain.document.model.DocumentApprovalHistoryResponse;
import com.malgn.domain.document.model.v1.RejectDocumentV1Request;
import com.malgn.domain.document.model.v1.SearchDocumentApprovalHistoryV1Request;
import com.malgn.domain.document.service.v1.DocumentApprovalHistoryV1Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/documents/approval")
public class DocumentApprovalV1Controller {

    private final DocumentApprovalHistoryV1Service approvalHistoryService;

    @GetMapping(path = "")
    public Page<DocumentApprovalHistoryResponse> search(SearchDocumentApprovalHistoryV1Request searchRequest,
        @PageableDefault(sort = "createdDate", size = 25, direction = Direction.DESC) Pageable pageable) {
        return approvalHistoryService.search(searchRequest, pageable);
    }
    @GetMapping(path = "{id:\\d+}")
    public DocumentApprovalHistoryResponse getById(@PathVariable long id) {
        return approvalHistoryService.getById(Id.of(DocumentApprovalHistory.class, id));
    }

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
