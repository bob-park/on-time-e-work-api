package com.malgn.domain.approval.service;

import java.util.List;

import com.malgn.domain.approval.model.ApprovalLineResponse;
import com.malgn.domain.approval.model.SearchApprovalLineRequest;

public interface ApprovalLineService {

    List<ApprovalLineResponse> getAll(SearchApprovalLineRequest searchRequest);

}
