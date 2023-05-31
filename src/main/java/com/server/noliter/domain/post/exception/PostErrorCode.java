package com.server.noliter.domain.post.exception;

import com.server.noliter.global.exception.ErrorCode;

public enum PostErrorCode implements ErrorCode {
    POST_NOT_FOUND(400, "POST_001", "해당 게시물을 찾을 수 없습니다.");
    private final int statusCode;
    private final String errorCode;
    private final String message;
    PostErrorCode(int statusCode, String errorCode, String message){
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
