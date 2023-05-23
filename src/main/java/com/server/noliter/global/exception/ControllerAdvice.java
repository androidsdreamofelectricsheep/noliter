package com.server.noliter.global.exception;

import com.server.noliter.global.exception.dto.response.ExceptionResponse;
import com.server.noliter.support.logging.UnhandledErrorLogging;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(NoliterException.class)
    public ResponseEntity<ExceptionResponse> handlerException(NoliterException e){
        int statusCode = e.getStatusCode();
        ExceptionResponse response = ExceptionResponse.from(e.getErrorCode());

        return ResponseEntity.status(statusCode).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(){
        return convert(GlobalErrorCode.VALIDATION_ERROR);
    }

    @ExceptionHandler({NoHandlerFoundException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ExceptionResponse> notSupportedUriException() {
        return convert(GlobalErrorCode.URI_NOT_SUPPORTED);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> notSupportedMethodException() {
        return convert(GlobalErrorCode.METHOD_NOT_SUPPORTED);
    }

    @UnhandledErrorLogging
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleAnyException() {
        return convert(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> convert(ErrorCode errorCode){
        int statusCode = errorCode.getStatusCode();
        ExceptionResponse response = ExceptionResponse.from(errorCode.getErrorCode());

        return ResponseEntity.status(statusCode).body(response);
    }

}
