package com.malgn.domain.user.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.math.BigDecimal;

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

import com.malgn.common.entity.BaseTimeEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users_leave_entries")
public class UserLeaveEntry extends BaseTimeEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userUniqueId;

    private Integer year;
    private BigDecimal totalLeaveDays;
    private BigDecimal usedLeaveDays;
    private BigDecimal totalCompLeaveDays;
    private BigDecimal usedCompLeaveDays;

    @Builder
    private UserLeaveEntry(Long id, String userUniqueId, Integer year, BigDecimal totalLeaveDays,
        BigDecimal usedLeaveDays,
        BigDecimal totalCompLeaveDays, BigDecimal usedCompLeaveDays) {

        checkArgument(StringUtils.isNotBlank(userUniqueId), "userId must be provided.");
        checkArgument(isNotEmpty(year), "year must be provided.");
        checkArgument(isNotEmpty(totalLeaveDays), "totalLeaveDays must be provided.");

        this.id = id;
        this.userUniqueId = userUniqueId;
        this.year = year;
        this.totalLeaveDays = totalLeaveDays;
        this.usedLeaveDays = defaultIfNull(usedLeaveDays, BigDecimal.ZERO);
        this.totalCompLeaveDays = defaultIfNull(totalCompLeaveDays, BigDecimal.ZERO);
        this.usedCompLeaveDays = defaultIfNull(usedCompLeaveDays, BigDecimal.ZERO);
    }

    /*
     * 편의 메서드
     */
    public BigDecimal availableDays() {
        return getTotalLeaveDays().subtract(getUsedLeaveDays());
    }

    public BigDecimal availableCompDays() {
        return getTotalCompLeaveDays().subtract(getUsedCompLeaveDays());
    }
}
