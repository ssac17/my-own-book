package com.myownbook.api.exception;

import com.myownbook.api.exception.errorcode.ErrorCode;

public class InvalidRefreshTokenException extends RuntimeException{
    private final String message;

    public InvalidRefreshTokenException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
