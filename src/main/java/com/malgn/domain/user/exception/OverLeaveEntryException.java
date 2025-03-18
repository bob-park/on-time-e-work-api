package com.malgn.domain.user.exception;

public class OverLeaveEntryException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Over leave entry";

    public OverLeaveEntryException() {
        super(DEFAULT_MESSAGE);
    }

    public OverLeaveEntryException(String message) {
        super(DEFAULT_MESSAGE + " - " + message);
    }

    public OverLeaveEntryException(String message, Throwable cause) {
        super(DEFAULT_MESSAGE + " - " + message, cause);
    }

    public OverLeaveEntryException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public OverLeaveEntryException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(DEFAULT_MESSAGE + " - " + message, cause, enableSuppression, writableStackTrace);
    }
}
