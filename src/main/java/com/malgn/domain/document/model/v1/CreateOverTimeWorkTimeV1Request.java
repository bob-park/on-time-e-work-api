package com.malgn.domain.document.model.v1;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.time.LocalDateTime;

import com.malgn.domain.document.model.CreateOverTimeWorkTimeRequest;

public record CreateOverTimeWorkTimeV1Request(LocalDateTime startDate,
                                              LocalDateTime endDate,
                                              String userUniqueId,
                                              String username,
                                              String contents,
                                              Boolean isDayOff,
                                              Boolean isExtraPayment)
    implements CreateOverTimeWorkTimeRequest {

    public CreateOverTimeWorkTimeV1Request {
        isDayOff = defaultIfNull(isDayOff, false);
        isExtraPayment = defaultIfNull(isExtraPayment, false);
    }
}
