package com.malgn.domain.document.entity.type;

import lombok.ToString;

@ToString
public enum DocumentStatus {

    WAITING,
    PROCEEDING,
    APPROVED,
    REJECTED,
    CANCELLED;
}
