package com.malgn.domain.user.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

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

import org.apache.commons.lang3.ObjectUtils;

import com.google.common.base.Preconditions;

import com.malgn.common.entity.BaseEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "leave_days_policies")
public class LeaveDaysPolicy extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer continuousYears;
    private Integer totalLeaveDays;

    @Builder
    private LeaveDaysPolicy(Long id, Integer continuousYears, Integer totalLeaveDays) {

        checkArgument(isNotEmpty(continuousYears), "continuousYears must be provided.");
        checkArgument(isNotEmpty(totalLeaveDays), "continuousYears must be provided.");

        this.id = id;
        this.continuousYears = continuousYears;
        this.totalLeaveDays = totalLeaveDays;
    }
}
