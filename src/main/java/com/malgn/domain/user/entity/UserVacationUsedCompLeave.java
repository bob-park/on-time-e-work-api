package com.malgn.domain.user.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

import com.malgn.common.entity.BaseTimeEntity;
import com.malgn.domain.document.entity.VacationDocument;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users_vacations_used_comp_leaves")
public class UserVacationUsedCompLeave extends BaseTimeEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private VacationDocument document;

    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comp_leave_entry_id")
    private UserCompLeaveEntry compLeaveEntry;

    private BigDecimal usedDays;

    @Builder
    private UserVacationUsedCompLeave(Long id, BigDecimal usedDays) {
        this.id = id;
        this.usedDays = usedDays;
    }

    /*
     * 편의 메서드
     */
    public void updateDocument(VacationDocument document) {
        this.document = document;
    }

    public void updateCompLeaveEntry(UserCompLeaveEntry compLeaveEntry) {
        this.compLeaveEntry = compLeaveEntry;
    }

    public void updateUsedDays(BigDecimal usedDays) {
        this.usedDays = usedDays;
    }
}
