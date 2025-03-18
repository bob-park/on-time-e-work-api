package com.malgn.domain.document.model;

import java.time.LocalDate;

import com.malgn.domain.document.entity.type.ApprovalStatus;

public interface SearchDocumentApprovalHistoryRequest {

    String userUniqueId();

    ApprovalStatus status();

    LocalDate createdDateFrom();

    LocalDate createdDateTo();

}
