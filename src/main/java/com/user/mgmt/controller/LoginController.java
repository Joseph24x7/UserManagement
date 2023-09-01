package com.user.mgmt.controller;

import com.user.mgmt.entity.UserEntity;
import com.user.mgmt.request.LoginRequest;
import com.user.mgmt.request.MyProfileRequest;
import com.user.mgmt.request.VerifyEmailRequest;
import com.user.mgmt.service.LoginService;
import com.user.mgmt.util.CommonUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final Validator validator;

    @GetMapping("/user-info")
    public UserEntity saveUserInfo(Authentication authentication, @RequestParam String role) {
        LoginRequest loginRequest = getValidatedLoginRequest((String) authentication.getCredentials(), role);
        return loginService.saveUserInfo(loginRequest, CommonUtil.LOGIN_WITH_GOOGLE_OAUTH_2);
    }

    @PostMapping("/login-with-access-code")
    public void loginWithAccessCode(@RequestBody @Valid LoginRequest loginRequest) {
        loginService.saveUserInfo(loginRequest, CommonUtil.LOGIN_WITH_ACCESS_CODE);
    }

    @PutMapping("/update-user-info")
    public UserEntity saveUserInfo(@RequestBody @Valid MyProfileRequest myProfileRequest) {
        return loginService.updateUserInfo(myProfileRequest);
    }


    @PostMapping("/verify-access-code")
    public String verifyAccessCode(@RequestBody @Valid VerifyEmailRequest emailRequest) {
        return loginService.verifyAccessCode(emailRequest);
    }

    private LoginRequest getValidatedLoginRequest(String email, String role) {
        LoginRequest loginRequest = new LoginRequest(email, role);
        Set<ConstraintViolation<Object>> violations = validator.validate(loginRequest);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        return loginRequest;
    }
}

