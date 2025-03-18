package com.malgn.domain.document.repository.query;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.model.SearchDocumentApprovalHistoryRequest;

public interface DocumentApprovalHistoryQueryRepository {

    Optional<DocumentApprovalHistory> getHistory(Id<DocumentApprovalHistory, Long> id);

    Page<DocumentApprovalHistory> search(SearchDocumentApprovalHistoryRequest searchRequest, Pageable pageable);

}
