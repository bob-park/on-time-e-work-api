package com.malgn.domain.document.model.v1;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

import com.malgn.domain.approval.model.ApprovalLineResponse;
import com.malgn.domain.approval.model.v1.ApprovalLineV1Response;
import com.malgn.domain.document.entity.DocumentApprovalHistory;
import com.malgn.domain.document.entity.VacationDocument;
import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.entity.type.VacationSubType;
import com.malgn.domain.document.entity.type.VacationType;
import com.malgn.domain.document.model.DocumentApprovalHistoryResponse;
import com.malgn.domain.user.model.UserCompLeaveEntryResponse;
import com.malgn.domain.document.model.VacationDocumentResponse;
import com.malgn.domain.user.entity.UserVacationUsedCompLeave;
import com.malgn.domain.user.model.v1.UserCompLeaveEntryV1Response;

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
                                         List<UserCompLeaveEntryResponse> usedCompLeaveEntries,
                                         List<DocumentApprovalHistoryResponse> approvalHistories,
                                         LocalDateTime createdDate,
                                         String createdBy,
                                         LocalDateTime lastModifiedDate,
                                         String lastModifiedBy)
    implements VacationDocumentResponse {

    public static VacationDocumentResponse from(VacationDocument entity) {
        return from(entity, false);
    }

    public static VacationDocumentResponse from(VacationDocument entity, boolean detail) {
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
            .usedCompLeaveEntries(detail ?
                entity.getUsedCompLeaves().stream()
                    .map(UserVacationUsedCompLeave::getCompLeaveEntry)
                    .map(UserCompLeaveEntryV1Response::from)
                    .toList()
                : null)
            .approvalHistories(detail ?
                entity.getApprovalHistories().stream()
                    .map(DocumentApprovalHistoryV1Response::from)
                    .toList()
                : null)
            .createdDate(entity.getCreatedDate())
            .createdBy(entity.getCreatedBy())
            .lastModifiedDate(entity.getLastModifiedDate())
            .lastModifiedBy(entity.getLastModifiedBy())
            .build();
    }
}
