package com.malgn.domain.user.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.entity.BaseEntity;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users_comp_leave_entries")
public class UserCompLeaveEntry extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userUniqueId;
    private String contents;
    private String description;
    private LocalDate effectiveDate;
    private BigDecimal leaveDays;
    private BigDecimal usedDays;

    @Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "compLeaveEntry")
    private List<UserVacationUsedCompLeave> usedCompLeaves = new ArrayList<>();

    @Builder
    private UserCompLeaveEntry(Long id, String userUniqueId, String contents, String description,
        LocalDate effectiveDate, BigDecimal leaveDays, BigDecimal usedDays) {

        checkArgument(StringUtils.isNotBlank(userUniqueId), "userUniqueId must be provided.");
        checkArgument(StringUtils.isNotBlank(contents), "contents must be provided.");
        checkArgument(isNotEmpty(effectiveDate), "effectiveDate must be provided.");
        checkArgument(isNotEmpty(leaveDays), "leaveDays must be provided.");

        this.id = id;
        this.userUniqueId = userUniqueId;
        this.contents = contents;
        this.description = description;
        this.effectiveDate = effectiveDate;
        this.leaveDays = leaveDays;
        this.usedDays = defaultIfNull(usedDays, BigDecimal.ZERO);
    }

    /*
     * 편의 메서드
     */
    public void addUsedCompLeave(UserVacationUsedCompLeave usedCompLeave) {
        usedCompLeave.updateCompLeaveEntry(this);

        getUsedCompLeaves().add(usedCompLeave);
    }

    public BigDecimal availableDays() {
        return leaveDays.subtract(usedDays);
    }

    public void restoreUsedDays(BigDecimal restoreDays) {
        this.usedDays = getUsedDays().subtract(restoreDays);
    }
}
