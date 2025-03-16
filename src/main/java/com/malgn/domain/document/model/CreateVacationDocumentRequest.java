package com.malgn.domain.document.model;

import java.time.LocalDate;

import com.malgn.domain.document.entity.type.VacationSubType;
import com.malgn.domain.document.entity.type.VacationType;

public interface CreateVacationDocumentRequest {

    String userUniqueId();

    VacationType vacationType();

    VacationSubType vacationSubType();

    LocalDate startDate();

    LocalDate endDate();

    String reason();

}
