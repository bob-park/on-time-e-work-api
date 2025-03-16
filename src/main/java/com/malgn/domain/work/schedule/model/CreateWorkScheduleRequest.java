package com.malgn.domain.work.schedule.model;

import java.time.LocalDate;

public interface CreateWorkScheduleRequest {

    String contents();

    String description();

    LocalDate startDate();

    LocalDate endDate();

    Boolean isRepeated();

    Boolean isClosed();

}
