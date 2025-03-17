package com.malgn.domain.approval.service;

import java.util.List;

import com.malgn.domain.approval.model.ApprovalLineResponse;

public interface ApprovalLineService {

    List<ApprovalLineResponse> getAll(Long teamId);

}
