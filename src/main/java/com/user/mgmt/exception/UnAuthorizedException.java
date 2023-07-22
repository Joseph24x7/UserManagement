package com.user.mgmt.exception;

import lombok.Getter;

@Getter
public class UnAuthorizedException extends RuntimeException {

    private final ErrorEnums errorEnums;

    public UnAuthorizedException(ErrorEnums errorEnums) {
        super(errorEnums.getErrorDescription());
        this.errorEnums = errorEnums;
    }

    public UnAuthorizedException(ErrorEnums errorEnums, String errorMessage, Throwable e) {
        super(errorMessage, e);
        this.errorEnums = errorEnums;
    }

}
