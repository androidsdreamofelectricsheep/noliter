package com.server.noliter.global.exception;

import lombok.Getter;

@Getter
public class NoliterException extends RuntimeException{
    private final int statusCode;
    private final String errorCode;
    private final String message;

    public NoliterException(ErrorCode code){
        statusCode = code.getStatusCode();
        errorCode = code.getErrorCode();
        message = code.getMessage();
    }
}
