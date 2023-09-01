package com.user.mgmt.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ErrorEnums errorEnums;

    public BadRequestException(ErrorEnums errorEnums) {
        super(errorEnums.getErrorDescription());
        this.errorEnums = errorEnums;
    }

}
