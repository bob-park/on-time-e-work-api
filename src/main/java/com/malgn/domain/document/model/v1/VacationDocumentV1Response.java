package com.malgn.domain.document.model.v1;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;

import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.entity.type.VacationSubType;
import com.malgn.domain.document.entity.type.VacationType;
import com.malgn.domain.document.model.VacationDocumentResponse;

@Builder
public record VacationDocumentV1Response(Long id,
                                         DocumentType type,
                                         DocumentStatus status,
                                         String userUniqueId,
                                         VacationType vacationType,
                                         VacationSubType vacationSubType,
                                         LocalDate startDate,
                                         LocalDate endDate,
                                         BigDecimal usedDays,
                                         String reason,
                                         LocalDateTime createdDate,
                                         String createdBy,
                                         LocalDateTime lastModifiedDate,
                                         String lastModifiedBy)
    implements VacationDocumentResponse {

    public static VacationDocumentResponse from(VacationDocument entity) {
        return VacationDocumentV1Response.builder()
            .id(entity.getId())
            .type(entity.getType())
            .status(entity.getStatus())
            .userUniqueId(entity.getUserUniqueId())
            .vacationType(entity.getVacationType())
            .vacationSubType(entity.getVacationSubType())
            .startDate(entity.getStartDate())
            .endDate(entity.getEndDate())
            .usedDays(entity.getUsedDays())
            .reason(entity.getReason())
            .createdDate(entity.getCreatedDate())
            .createdBy(entity.getCreatedBy())
            .lastModifiedDate(entity.getLastModifiedDate())
            .lastModifiedBy(entity.getLastModifiedBy())
            .build();
    }
}
