package com.malgn.domain.document.entity;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.List;

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

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "overtime_works_times_reports")
public class OvertimeWorkTimeReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private OvertimeWorkTime time;

    private String report;

    @Builder
    private OvertimeWorkTimeReport(Long id, String report) {

        checkArgument(StringUtils.isNotBlank(report), "report must be provided.");

        this.id = id;
        this.report = report;
    }

    /*
     * 편의 메서드
     */
    public void updateTime(OvertimeWorkTime time) {
        this.time = time;
    }

}
