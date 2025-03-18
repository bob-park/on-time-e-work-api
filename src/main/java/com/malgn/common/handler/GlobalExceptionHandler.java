package com.malgn.common.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.malgn.common.model.ApiResult;
import com.malgn.domain.user.exception.OverLeaveEntryException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OverLeaveEntryException.class)
    public <T> ApiResult<T> overLeaveEntryException(OverLeaveEntryException e) {
        return ApiResult.error("BAD_REQUEST", e.getMessage(), e);
    }

}
