package com.malgn.domain.work.schedule.model.v1;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.time.LocalDate;

import lombok.Builder;

import com.malgn.domain.work.schedule.model.CreateWorkScheduleRequest;

@Builder
public record CreateWorkScheduleV1Request(String contents,
                                          String description,
                                          LocalDate startDate,
                                          LocalDate endDate,
                                          Boolean isRepeated,
                                          Boolean isClosed)
    implements CreateWorkScheduleRequest {

    public CreateWorkScheduleV1Request {
        isRepeated = defaultIfNull(isNotEmpty(isRepeated), false);
        isClosed = defaultIfNull(isNotEmpty(isClosed), false);
    }
}
