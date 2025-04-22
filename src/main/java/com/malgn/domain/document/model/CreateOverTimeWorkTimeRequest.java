package com.malgn.domain.document.model;

import java.time.LocalDateTime;

public interface CreateOverTimeWorkTimeRequest {

    LocalDateTime startDate();

    LocalDateTime endDate();

    String userUniqueId();

    String username();

    String contents();

    Boolean isDayOff();

    Boolean isExtraPayment();

}
