package com.malgn.domain.document.model;

import java.time.LocalDate;

import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.VacationType;

public interface SearchVacationDocumentRequest {

    String userUniqueId();

    DocumentStatus status();

    VacationType vacationType();

    LocalDate startDateFrom();

    LocalDate endDateTo();

}
