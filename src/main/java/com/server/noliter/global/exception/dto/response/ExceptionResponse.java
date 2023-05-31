package com.server.noliter.global.exception.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
@AllArgsConstructor
public class ExceptionResponse {
    private final String message;

    public static ExceptionResponse from(String e){
        return new ExceptionResponse(e);
    }

    public static ExceptionResponse from(MethodArgumentNotValidException e){
        StringBuilder message = new StringBuilder();

        for(FieldError error:e.getFieldErrors()){
            message.append(error.getDefaultMessage()).append(" ");
        }
        return new ExceptionResponse(new String(message));
    }

    public String getMessage(){
        return message;
    }
}
