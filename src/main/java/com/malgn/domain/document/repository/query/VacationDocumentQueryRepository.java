package com.malgn.domain.document.repository.query;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.model.SearchVacationDocumentRequest;

public interface VacationDocumentQueryRepository {

    Page<VacationDocument> search(SearchVacationDocumentRequest searchRequest, Pageable pageable);

    Optional<VacationDocument> getDocumentById(Id<VacationDocument, Long> id);

}
