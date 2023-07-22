package com.user.mgmt.controller;

import com.user.mgmt.exception.BadRequestException;
import com.user.mgmt.exception.ErrorEnums;
import com.user.mgmt.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException exception) {
        log.error("BadRequestException occurred: ", exception);
        return populateException(exception.getErrorEnums());
    }

    private ErrorResponse populateException(ErrorEnums errorEnums) {
        return ErrorResponse.builder().errorType(errorEnums.getErrorCode()).errorDescription(errorEnums.getErrorDescription()).build();
    }

}
