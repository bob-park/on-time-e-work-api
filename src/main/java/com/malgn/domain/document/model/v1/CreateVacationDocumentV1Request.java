package com.malgn.domain.document.model.v1;

import java.time.LocalDate;

import lombok.Builder;

import com.malgn.domain.document.entity.type.VacationSubType;
import com.malgn.domain.document.entity.type.VacationType;
import com.malgn.domain.document.model.CreateVacationDocumentRequest;

@Builder
public record CreateVacationDocumentV1Request(VacationType vacationType,
                                              VacationSubType vacationSubType,
                                              LocalDate startDate,
                                              LocalDate endDate,
                                              String reason)
    implements CreateVacationDocumentRequest {
}
