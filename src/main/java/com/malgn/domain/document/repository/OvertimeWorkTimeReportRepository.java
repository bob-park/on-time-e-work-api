package com.malgn.domain.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.document.entity.OvertimeWorkTimeReport;

public interface OvertimeWorkTimeReportRepository extends JpaRepository<OvertimeWorkTimeReport, Long> {
}
