package com.server.noliter.global.exception;

import com.server.noliter.global.exception.dto.ErrorDto;
import com.server.noliter.global.exception.dto.response.ExceptionResponse;
import com.server.noliter.support.logging.UnhandledErrorLogging;
import com.server.noliter.web.controller.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.NoSuchElementException;

@Slf4j
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



    // @ExceptionHandler(RuntimeException.class)
    // public ResponseEntity<Response<?>> allOtherException(RuntimeException e) {
    //     ErrorDto error = new ErrorDto("SERVER", e.getMessage());
    //
    //     return new ResponseEntity<>(new Response<>(null, error), HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    //
    // @ExceptionHandler(AccessDeniedException.class)
    // public ResponseEntity<Response<?>> accessDeny(AccessDeniedException e) {
    //     ErrorDto error = new ErrorDto("CLIENT", e.getMessage());
    //
    //     return new ResponseEntity<>(new Response<>(null, error), HttpStatus.UNAUTHORIZED);
    // }
    //
    // @ExceptionHandler(IllegalArgumentException.class)
    // public ResponseEntity<Response<?>> badRequest(IllegalArgumentException e) {
    //     ErrorDto error = new ErrorDto("CLIENT", e.getMessage());
    //
    //     return new ResponseEntity<>(new Response<>(null, error), HttpStatus.BAD_REQUEST);
    // }
    //
    // @ExceptionHandler(NoSuchElementException.class)
    // public ResponseEntity<Response<?>> notFound(NoSuchElementException e) {
    //     ErrorDto error = new ErrorDto("CLIENT", e.getMessage());
    //
    //     return new ResponseEntity<>(new Response<>(null, error), HttpStatus.NOT_FOUND);
    // }
    //
    // @ExceptionHandler(IllegalStateException.class)
    // public ResponseEntity<Response<?>> server(IllegalStateException e) {
    //     ErrorDto error = new ErrorDto("SERVER", e.getMessage());
    //
    //     return new ResponseEntity<>(new Response<>(null, error), HttpStatus.INTERNAL_SERVER_ERROR);
    // }
}
