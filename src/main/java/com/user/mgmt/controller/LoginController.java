package com.user.mgmt.controller;

import com.user.mgmt.entity.UserEntity;
import com.user.mgmt.request.LoginRequest;
import com.user.mgmt.request.MyProfileRequest;
import com.user.mgmt.request.VerifyEmailRequest;
import com.user.mgmt.service.LoginService;
import com.user.mgmt.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final Validator validator;

    @GetMapping("/user-info")
    public UserEntity saveUserInfo(HttpServletRequest httpServletRequest) {
        LoginRequest loginRequest = getValidatedLoginRequest(httpServletRequest);
        return loginService.saveUserInfo(loginRequest, CommonUtil.LOGIN_WITH_GOOGLE_OAUTH_2);
    }

    @PostMapping("/login-with-access-code")
    public void loginWithAccessCode(@RequestBody @Valid LoginRequest loginRequest) {
        loginService.saveUserInfo(loginRequest, CommonUtil.LOGIN_WITH_ACCESS_CODE);
    }

    @PutMapping("/update-user-info")
    public UserEntity saveUserInfo(@RequestBody @Valid MyProfileRequest myProfileRequest, HttpServletRequest httpServletRequest) {
        return loginService.updateUserInfo(myProfileRequest, String.valueOf(httpServletRequest.getAttribute("email")));
    }


    @PostMapping("/verify-access-code")
    public String verifyAccessCode(@RequestBody @Valid VerifyEmailRequest emailRequest) {
        return loginService.verifyAccessCode(emailRequest);
    }

    private LoginRequest getValidatedLoginRequest(HttpServletRequest httpServletRequest) {
        LoginRequest loginRequest = new LoginRequest((String) httpServletRequest.getAttribute("email"), (String) httpServletRequest.getAttribute("role"));
        Set<ConstraintViolation<Object>> violations = validator.validate(loginRequest);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
        return loginRequest;
    }
}

