package com.malgn.domain.work.schedule.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.entity.BaseEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "work_schedules")
public class WorkSchedule extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean isRepeated;
    private Boolean isClosed;

    @Builder
    private WorkSchedule(Long id, String contents, String description, LocalDate startDate, LocalDate endDate,
        Boolean isRepeated, Boolean isClosed) {

        checkArgument(StringUtils.isNotBlank(contents), "Content must be provided.");
        checkArgument(isNotEmpty(startDate), "startDate must be provided.");
        checkArgument(isNotEmpty(endDate), "endDate must be provided.");
        checkArgument(
            startDate.isEqual(endDate) || startDate.isBefore(endDate),
            "startDate must be less than endDate.");

        this.id = id;
        this.contents = contents;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isRepeated = defaultIfNull(isRepeated, false);
        this.isClosed = defaultIfNull(isClosed, false);
    }

    /*
     * 편의 메서드
     */

    /*
     * custom getter
     */
    public boolean isRepeated() {
        return Boolean.TRUE.equals(isRepeated);
    }

    public boolean isClosed() {
        return Boolean.TRUE.equals(isClosed);
    }
}
