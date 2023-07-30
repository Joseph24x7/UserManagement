package com.user.mgmt.controller;

import com.user.mgmt.exception.BadRequestException;
import com.user.mgmt.exception.ErrorEnums;
import com.user.mgmt.exception.ErrorResponse;
import com.user.mgmt.exception.UnAuthorizedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException exception) {
        log.error("BadRequestException occurred: ", exception);
        return populateException(exception.getErrorEnums());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthorizationException(UnAuthorizedException exception) {
        log.error("AuthorizationException occurred: ", exception);
        return populateException(exception.getErrorEnums());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        log.error("Exception occurred: ", exception);
        return populateException(ErrorEnums.GENERAL_EXCEPTION);
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(Exception exception) {
        log.error("ConstraintViolationException occurred: ", exception);
        List<String> errors = new ArrayList<>();
        if (exception instanceof ConstraintViolationException constraintEx) {
            for (ConstraintViolation<?> violation : constraintEx.getConstraintViolations()) {
                errors.add(violation.getMessage());
            }
        } else if (exception instanceof MethodArgumentNotValidException validationEx) {
            for (FieldError error : validationEx.getBindingResult().getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }
        }
        return populateException(HttpStatus.BAD_REQUEST.getReasonPhrase(), List.copyOf(errors).toString());
    }

    private ErrorResponse populateException(ErrorEnums errorEnums) {
        return ErrorResponse.builder().errorType(errorEnums.getErrorCode()).errorDescription(errorEnums.getErrorDescription()).build();
    }

    private ErrorResponse populateException(String errorCode, String errorDescription) {
        return ErrorResponse.builder().errorType(errorCode).errorDescription(errorDescription).build();
    }

}
