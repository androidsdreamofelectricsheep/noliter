package com.server.noliter.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    URI_NOT_SUPPORTED(404, "URI_NOT_SUPPORTED", "지원하지 않는 URI 요청입니다."),

    METHOD_NOT_SUPPORTED(405, "METHOD_NOT_SUPPORTED", "지원하지 않는 메서드 요청입니다."),
    VALIDATION_ERROR(400, "VALIDATION_001", "잘못된 요청입니다."),

    INTERNAL_SERVER_ERROR(500, "SERVER_001", "내부 서버 오류입니다.");

    private final int statusCode;

    private final String errorCode;

    private final String message;
}
