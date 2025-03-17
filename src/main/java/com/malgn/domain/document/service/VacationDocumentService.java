package com.malgn.domain.document.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.model.CreateVacationDocumentRequest;
import com.malgn.domain.document.model.SearchVacationDocumentRequest;
import com.malgn.domain.document.model.VacationDocumentResponse;

public interface VacationDocumentService {

    VacationDocumentResponse createDocument(CreateVacationDocumentRequest createRequest);

    Page<VacationDocumentResponse> search(SearchVacationDocumentRequest searchRequest, Pageable pageable);

    VacationDocumentResponse getById(Id<VacationDocument, Long> id);

}
