package com.server.noliter.web.controller.dto;

import com.server.noliter.global.exception.dto.ErrorDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    T data;
    // ErrorDto error;
}
