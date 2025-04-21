package com.malgn.domain.document.entity;

import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;

import org.apache.commons.lang3.StringUtils;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "overtime_works_times")
public class OvertimeWorkTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private BigDecimal appliedHours;

    private String userUniqueId;
    private String username;

    private String contents;

    private Boolean isDayOff;

    private Boolean isExtraPayment;
    private BigDecimal appliedExtraPaymentHours;

    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private OvertimeWorkDocument document;

    @Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "time")
    private List<OvertimeWorkTimeReport> reports = new ArrayList<>();

    @Builder
    private OvertimeWorkTime(Long id, LocalDateTime startDate, LocalDateTime endDate, BigDecimal appliedHours,
        String userUniqueId, String username, String contents, Boolean isDayOff, Boolean isExtraPayment,
        BigDecimal appliedExtraPaymentHours) {

        checkArgument(isNotEmpty(startDate), "startDate must be provided.");
        checkArgument(isNotEmpty(endDate), "endDate must be provided.");
        checkArgument(StringUtils.isNotBlank(username), "username must be provided.");
        checkArgument(StringUtils.isNotBlank(contents), "username must be provided.");

        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.appliedHours = defaultIfNull(appliedHours, BigDecimal.ZERO);
        this.userUniqueId = userUniqueId;
        this.username = username;
        this.contents = contents;
        this.isDayOff = defaultIfNull(isDayOff, false);
        this.isExtraPayment = defaultIfNull(isExtraPayment, false);
        this.appliedExtraPaymentHours = defaultIfNull(appliedExtraPaymentHours, BigDecimal.ZERO);
    }

    /*
     * 편의 메서드
     */
    public void updateDocument(OvertimeWorkDocument document) {
        this.document = document;
    }

    public void addReport(OvertimeWorkTimeReport report) {
        report.updateTime(this);

        getReports().add(report);
    }
}
