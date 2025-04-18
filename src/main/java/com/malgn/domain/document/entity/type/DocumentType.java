package com.malgn.domain.document.entity.type;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum DocumentType {

    VACATION("휴가계"),
    OVERTIME_WORK("휴일근무보고서");

    private final String displayName;

    DocumentType(String displayName) {
        this.displayName = displayName;
    }
}
