package com.malgn.domain.document.service;

import com.malgn.domain.document.model.CreateVacationDocumentRequest;
import com.malgn.domain.document.model.VacationDocumentResponse;

public interface VacationDocumentService {

    VacationDocumentResponse createDocument(CreateVacationDocumentRequest createRequest);

}
