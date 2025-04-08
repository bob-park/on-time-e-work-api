package com.malgn.domain.document.model.v1;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;

import com.malgn.domain.document.entity.type.VacationSubType;
import com.malgn.domain.document.entity.type.VacationType;
import com.malgn.domain.document.model.CreateVacationDocumentRequest;

@Builder
public record CreateVacationDocumentV1Request(String userUniqueId,
                                              VacationType vacationType,
                                              VacationSubType vacationSubType,
                                              LocalDate startDate,
                                              LocalDate endDate,
                                              String reason,
                                              List<UsedCompLeaveEntryV1Request> compLeaveEntries)
    implements CreateVacationDocumentRequest {

    public CreateVacationDocumentV1Request {
        compLeaveEntries = defaultIfNull(compLeaveEntries, List.of());
    }
}
