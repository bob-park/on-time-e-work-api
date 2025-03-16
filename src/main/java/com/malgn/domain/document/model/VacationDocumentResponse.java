package com.malgn.domain.document.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.malgn.domain.document.entity.type.VacationSubType;
import com.malgn.domain.document.entity.type.VacationType;

public interface VacationDocumentResponse extends DocumentResponse {

    VacationType vacationType();

    VacationSubType vacationSubType();

    LocalDate startDate();

    LocalDate endDate();

    BigDecimal usedDays();

    String reason();

}
