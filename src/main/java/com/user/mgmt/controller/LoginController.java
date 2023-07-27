package com.user.mgmt.controller;

import com.user.mgmt.model.GoogleUserEntity;
import com.user.mgmt.request.MyProfileRequest;
import com.user.mgmt.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/user-info")
    public GoogleUserEntity saveUserInfo(HttpServletRequest httpServletRequest) {
        return loginService.saveUserInfo(String.valueOf(httpServletRequest.getAttribute("email")));
    }

    @PutMapping("/update-user-info")
    public GoogleUserEntity saveUserInfo(@RequestBody MyProfileRequest myProfileRequest, HttpServletRequest httpServletRequest) {
        return loginService.updateUserInfo(myProfileRequest, String.valueOf(httpServletRequest.getAttribute("email")));
    }

}


