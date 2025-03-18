package com.malgn.domain.document.model.v1;

import java.time.LocalDate;

import com.malgn.domain.document.entity.type.ApprovalStatus;
import com.malgn.domain.document.model.SearchDocumentApprovalHistoryRequest;

public record SearchDocumentApprovalHistoryV1Request(String userUniqueId,
                                                     ApprovalStatus status,
                                                     LocalDate createdDateFrom,
                                                     LocalDate createdDateTo)
    implements SearchDocumentApprovalHistoryRequest {

}
