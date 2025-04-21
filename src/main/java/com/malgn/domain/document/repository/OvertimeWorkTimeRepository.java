package com.malgn.domain.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.malgn.domain.document.entity.OvertimeWorkTime;

public interface OvertimeWorkTimeRepository extends JpaRepository<OvertimeWorkTime, Long> {
}
