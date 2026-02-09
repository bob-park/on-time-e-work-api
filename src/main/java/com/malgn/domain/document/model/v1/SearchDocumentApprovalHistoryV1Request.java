package com.malgn.domain.document.model.v1;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;

import com.malgn.domain.document.entity.type.ApprovalStatus;
import com.malgn.domain.document.model.SearchDocumentApprovalHistoryRequest;

@Builder(toBuilder = true)
public record SearchDocumentApprovalHistoryV1Request(String userUniqueId,
                                                     ApprovalStatus status,
                                                     LocalDate createdDateFrom,
                                                     LocalDate createdDateTo,
                                                     List<String> existUserIds)
    implements SearchDocumentApprovalHistoryRequest {

}
