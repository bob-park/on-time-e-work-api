package com.malgn.domain.document.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.entity.type.VacationSubType;
import com.malgn.domain.document.entity.type.VacationType;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "documents_vacations")
@DiscriminatorValue("VACATION")
public class VacationDocument extends Document {

    @Enumerated(EnumType.STRING)
    private VacationType vacationType;

    @Enumerated(EnumType.STRING)
    private VacationSubType vacationSubType;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal usedDays;

    private String reason;

    @Builder
    private VacationDocument(Long id, DocumentStatus status, String userUniqueId,
        VacationType vacationType, VacationSubType vacationSubType, LocalDate startDate, LocalDate endDate,
        BigDecimal usedDays, String reason) {

        super(id, DocumentType.VACATION, status, userUniqueId);

        checkArgument(isNotEmpty(vacationType), "vacationType must be provided.");
        checkArgument(isNotEmpty(startDate), "startDate must be provided.");
        checkArgument(isNotEmpty(endDate), "endDate must be provided.");
        checkArgument(StringUtils.isNotBlank(reason), "reason must be provided.");

        this.vacationType = vacationType;
        this.vacationSubType = vacationSubType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usedDays = usedDays;
        this.reason = reason;
    }

}
