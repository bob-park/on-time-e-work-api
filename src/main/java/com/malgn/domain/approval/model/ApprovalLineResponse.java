package com.malgn.domain.approval.model;

import java.util.List;

import com.malgn.common.model.CommonResponse;

public interface ApprovalLineResponse extends CommonResponse {

    Long id();

    ApprovalLineResponse parent();

    List<ApprovalLineResponse> children();

    Long teamId();

    String userUniqueId();

    String contents();

    void addChild(ApprovalLineResponse child);

}
