package com.malgn.domain.approval.controller.v1;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.malgn.domain.approval.model.ApprovalLineResponse;
import com.malgn.domain.approval.model.v1.SearchApprovalLineV1Request;
import com.malgn.domain.approval.service.v1.ApprovalLineV1Service;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/approval/lines")
public class ApprovalLineV1Controller {

    private final ApprovalLineV1Service approvalLineService;

    @GetMapping(path = "")
    public List<ApprovalLineResponse> getLines(SearchApprovalLineV1Request searchRequest) {
        return approvalLineService.getAll(searchRequest);
    }
}
