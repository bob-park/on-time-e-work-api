package com.malgn.domain.document.entity.type;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum DocumentStatus {

    DRAFT("초안"),
    WAITING("대기"),
    PROCEEDING("진행"),
    APPROVED("승인"),
    REJECTED("반려"),
    CANCELLED("취소");

    private final String displayName;

    DocumentStatus(String displayName) {
        this.displayName = displayName;
    }
}
