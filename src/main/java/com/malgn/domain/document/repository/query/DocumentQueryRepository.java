package com.malgn.domain.document.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.model.SearchDocumentRequest;

public interface DocumentQueryRepository {

    Page<Document> search(SearchDocumentRequest searchRequest, Pageable pageable);

}
