package com.server.noliter.domain.user.exception;

import com.server.noliter.global.exception.ErrorCode;

public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(400, "USER_001", "해당 사용자를 찾을 수 없습니다.");
    private final int statusCode;
    private final String errorCode;
    private final String message;

    UserErrorCode(int statusCode, String errorCode, String message) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = message;
    }


    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
