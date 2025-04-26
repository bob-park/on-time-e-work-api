package com.malgn.domain.document.service;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.OvertimeWorkDocument;
import com.malgn.domain.document.model.CreateOvertimeWorkDocumentRequest;
import com.malgn.domain.document.model.OvertimeWorkDocumentResponse;

public interface OvertimeWorkDocumentService {

    OvertimeWorkDocumentResponse create(CreateOvertimeWorkDocumentRequest createRequest);

    OvertimeWorkDocumentResponse getById(Id<OvertimeWorkDocument, Long> id);
}
