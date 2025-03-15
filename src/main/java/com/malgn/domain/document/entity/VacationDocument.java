package com.malgn.domain.document.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Convert;
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
import org.hibernate.annotations.Type;

import io.hypersistence.utils.hibernate.type.json.JsonType;

import com.malgn.domain.document.entity.converter.LongIdArrayConverter;
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
    private Integer usedDays;

    private String reason;

    @Type(JsonType.class)
    @Convert(converter = LongIdArrayConverter.class)
    private List<Long> usedCompLeaveIds = new ArrayList<>();

    @Builder
    private VacationDocument(Long id, DocumentStatus status, String writerId,
        VacationType vacationType, VacationSubType vacationSubType, LocalDate startDate, LocalDate endDate,
        Integer usedDays, String reason, List<Long> usedCompLeaveIds) {

        super(id, DocumentType.VACATION, status, writerId);

        checkArgument(isNotEmpty(vacationType), "vacationType must be provided.");
        checkArgument(isNotEmpty(startDate), "startDate must be provided.");
        checkArgument(isNotEmpty(endDate), "endDate must be provided.");
        checkArgument(StringUtils.isNotBlank(reason), "reason must be provided.");

        // compensatory leave 인 경우 유효성 체크
        if (vacationType == VacationType.COMPENSATORY) {
            checkArgument(usedCompLeaveIds != null && !usedCompLeaveIds.isEmpty(),
                "usedCompLeaveIds must be provided.");
        }

        this.vacationType = vacationType;
        this.vacationSubType = vacationSubType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usedDays = usedDays;
        this.reason = reason;
        this.usedCompLeaveIds = defaultIfNull(usedCompLeaveIds, List.of());
    }
}
