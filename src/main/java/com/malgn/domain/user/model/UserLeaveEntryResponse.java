package com.malgn.domain.user.model;

import java.math.BigDecimal;

public interface UserLeaveEntryResponse {

    Long id();

    String userUniqueId();

    Integer year();

    BigDecimal totalLeaveDays();

    BigDecimal usedLeaveDays();

    BigDecimal totalCompLeaveDays();

    BigDecimal usedCompLeaveDays();

}
