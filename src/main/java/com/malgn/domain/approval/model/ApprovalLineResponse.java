package com.malgn.domain.approval.model;

import java.util.List;

import com.malgn.common.model.CommonResponse;
import com.malgn.domain.document.entity.type.DocumentType;

public interface ApprovalLineResponse extends CommonResponse {

    Long id();

    ApprovalLineResponse parent();

    List<ApprovalLineResponse> children();

    DocumentType documentType();

    Long teamId();

    String userUniqueId();

    String contents();

    void addChild(ApprovalLineResponse child);

}
