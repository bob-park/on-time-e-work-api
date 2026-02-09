package com.malgn.domain.document.model.v1;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;

import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.VacationType;
import com.malgn.domain.document.model.SearchVacationDocumentRequest;

@Builder(toBuilder = true)
public record SearchVacationDocumentV1Request(String userUniqueId,
                                              DocumentStatus status,
                                              VacationType vacationType,
                                              LocalDate startDateFrom,
                                              LocalDate endDateTo,
                                              List<String> userUniqueIds)
    implements SearchVacationDocumentRequest {
}
