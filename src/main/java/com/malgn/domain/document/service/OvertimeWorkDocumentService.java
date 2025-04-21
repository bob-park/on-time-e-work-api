package com.malgn.domain.document.service;

import com.malgn.domain.document.model.CreateOvertimeWorkDocumentRequest;
import com.malgn.domain.document.model.OvertimeWorkDocumentResponse;

public interface OvertimeWorkDocumentService {

    OvertimeWorkDocumentResponse create(CreateOvertimeWorkDocumentRequest createRequest);
}
