package com.malgn.domain.document.repository.query;

import java.util.Optional;

import com.malgn.common.model.Id;
import com.malgn.domain.document.entity.Document;
import com.malgn.domain.document.entity.OvertimeWorkDocument;

public interface OvertimeWorkDocumentQueryRepository {

    Optional<OvertimeWorkDocument> findDocument(Id<? extends Document, Long> id);

}
