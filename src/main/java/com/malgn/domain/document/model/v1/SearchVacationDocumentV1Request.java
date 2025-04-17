package com.malgn.domain.document.model.v1;

import java.time.LocalDate;

import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.VacationType;
import com.malgn.domain.document.model.SearchVacationDocumentRequest;

public record SearchVacationDocumentV1Request(String userUniqueId,
                                              DocumentStatus status,
                                              VacationType vacationType,
                                              LocalDate startDateFrom,
                                              LocalDate endDateTo)
    implements SearchVacationDocumentRequest {
}
