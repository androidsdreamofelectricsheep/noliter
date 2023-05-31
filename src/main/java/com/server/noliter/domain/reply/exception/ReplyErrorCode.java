package com.server.noliter.domain.reply.exception;

import com.server.noliter.global.exception.ErrorCode;

public enum ReplyErrorCode implements ErrorCode {
    REPLY_NOT_FOUND(400, "REPLY_001", "해당 댓글을 찾을 수 없습니다.");

    private final int statusCode;
    private final String errorCode;
    private final String message;
    ReplyErrorCode(int statusCode, String errorCode, String message){
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
