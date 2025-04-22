package com.malgn.domain.document.model.v1;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.util.List;

import com.malgn.domain.document.model.CreateOvertimeWorkDocumentRequest;

public record CreateOvertimeWorkDocumentV1Request(String userUniqueId,
                                                  List<CreateOverTimeWorkTimeV1Request> times)
    implements CreateOvertimeWorkDocumentRequest {

    public CreateOvertimeWorkDocumentV1Request {
        times = defaultIfNull(times, List.of());
    }
}
