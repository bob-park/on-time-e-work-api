package com.malgn.domain.document.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

import org.apache.commons.lang3.StringUtils;

import com.malgn.domain.document.entity.type.DocumentStatus;
import com.malgn.domain.document.entity.type.DocumentType;
import com.malgn.domain.document.entity.type.VacationSubType;
import com.malgn.domain.document.entity.type.VacationType;
import com.malgn.domain.user.entity.UserVacationUsedCompLeave;

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

    @Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    private List<UserVacationUsedCompLeave> usedCompLeaves = new ArrayList<>();

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

    /*
     * 편의 메서드
     */
    public void addUsedCompLeave(UserVacationUsedCompLeave usedCompLeave) {

        usedCompLeave.updateDocument(this);

        getUsedCompLeaves().add(usedCompLeave);
    }

}
