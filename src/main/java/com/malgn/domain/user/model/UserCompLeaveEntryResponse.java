package com.malgn.domain.user.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.malgn.common.model.CommonResponse;

public interface UserCompLeaveEntryResponse extends CommonResponse {

    Long id();

    String contents();

    String description();

    LocalDate effectiveDate();

    BigDecimal leaveDays();

    BigDecimal usedDays();
}
