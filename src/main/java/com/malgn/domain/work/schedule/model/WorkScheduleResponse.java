package com.malgn.domain.work.schedule.model;

import java.time.LocalDate;

import com.malgn.common.model.CommonResponse;

public interface WorkScheduleResponse extends CommonResponse {

    Long id();

    String contents();

    String description();

    LocalDate startDate();

    LocalDate endDate();

    Boolean isRepeated();

    Boolean isClosed();

}
