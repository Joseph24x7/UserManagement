package com.user.mgmt.exception;

public enum ErrorEnums {

    USER_ALREADY_SIGNED_UP("USER_ALREADY_SIGNED_UP", "User Already Signed Up. Do you want to sign in?"),
    AUTHORIZATION_REQUIRED("AUTHORIZATION_REQUIRED", "Authorization token is required. Please provide a valid token to proceed."),
    AUTHORIZATION_FAILED("AUTHORIZATION_FAILED", "Username or Password is incorrect. Please contact the administrator."),
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found."),
    INVALID_ACCESS_CODE("INVALID_ACCESS_CODE", "Invalid Access Code. Try Again."),
    GENERAL_EXCEPTION("GENERAL_EXCEPTION", "General Exception Occurred. Try Again Later.");
    private final String errorCode;
    private final String errorDescription;

    ErrorEnums(String errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

}
