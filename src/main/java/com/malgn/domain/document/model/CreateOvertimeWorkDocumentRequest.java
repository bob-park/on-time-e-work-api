package com.malgn.domain.document.model;

import java.util.List;

public interface CreateOvertimeWorkDocumentRequest {

    String userUniqueId();

    List<? extends CreateOverTimeWorkTimeRequest> times();

}
