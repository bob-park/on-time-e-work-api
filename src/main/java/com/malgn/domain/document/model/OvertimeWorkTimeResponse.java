package com.malgn.domain.document.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OvertimeWorkTimeResponse {
    Long id();

    LocalDateTime startDate();

    LocalDateTime endDate();

    BigDecimal appliedHours();

    String userUniqueId();

    String username();

    String contents();

    Boolean isDayOff();

    Boolean isExtraPayment();

    BigDecimal appliedExtraPaymentHours();
}
