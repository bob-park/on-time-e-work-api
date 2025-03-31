package com.malgn.domain.document.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.domain.document.model.DocumentResponse;
import com.malgn.domain.document.model.SearchDocumentRequest;

public interface DocumentService {

    Page<DocumentResponse> search(SearchDocumentRequest searchRequest, Pageable pageable);

}
