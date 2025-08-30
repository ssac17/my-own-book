package com.myownbook.api.exception;

import com.myownbook.api.exception.errorcode.ErrorCode;

public class RestApiException extends RuntimeException{

    private final ErrorCode errorCode;

    public RestApiException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
