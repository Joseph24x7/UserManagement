package com.user.mgmt.controller;

import com.user.mgmt.model.UserEntity;
import com.user.mgmt.request.LoginWithEmailRequest;
import com.user.mgmt.request.MyProfileRequest;
import com.user.mgmt.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/user-info")
    public UserEntity saveUserInfo(HttpServletRequest httpServletRequest) {
        return loginService.saveUserInfo(String.valueOf(httpServletRequest.getAttribute("email")));
    }

    @PutMapping("/update-user-info")
    public UserEntity saveUserInfo(@RequestBody MyProfileRequest myProfileRequest, HttpServletRequest httpServletRequest) {
        return loginService.updateUserInfo(myProfileRequest, String.valueOf(httpServletRequest.getAttribute("email")));
    }

    @PostMapping("/login-with-access-code")
    public void loginWithAccessCode(@RequestBody LoginWithEmailRequest emailRequest) {
        loginService.loginWithAccessCode(emailRequest.getEmail());
    }

    @PostMapping("/verify-access-code")
    public String verifyAccessCode(@RequestBody LoginWithEmailRequest emailRequest) {
        return loginService.verifyAccessCode(emailRequest);
    }

}
