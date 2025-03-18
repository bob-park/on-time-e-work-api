package com.malgn.domain.document.repository.query;

import java.util.Optional;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.DocumentApprovalHistory;

public interface DocumentApprovalHistoryQueryRepository {

    Optional<DocumentApprovalHistory> getHistory(Id<DocumentApprovalHistory, Long> id);

}
