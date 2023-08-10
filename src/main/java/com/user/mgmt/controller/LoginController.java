package com.user.mgmt.controller;

import com.user.mgmt.util.CommonUtil;
import com.user.mgmt.entity.UserEntity;
import com.user.mgmt.request.LoginWithEmailRequest;
import com.user.mgmt.request.MyProfileRequest;
import com.user.mgmt.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/user-info")
    public UserEntity saveUserInfo(HttpServletRequest httpServletRequest) {
        return loginService.saveUserInfo(String.valueOf(httpServletRequest.getAttribute("email")), CommonUtil.LOGIN_WITH_GOOGLE_OAUTH_2);
    }

    @PostMapping("/login-with-access-code")
    public void loginWithAccessCode(@RequestBody @Valid LoginWithEmailRequest emailRequest) {
        loginService.saveUserInfo(emailRequest.getEmail(), CommonUtil.LOGIN_WITH_ACCESS_CODE);
    }

    @PutMapping("/update-user-info")
    public UserEntity saveUserInfo(@RequestBody @Valid MyProfileRequest myProfileRequest, HttpServletRequest httpServletRequest) {
        return loginService.updateUserInfo(myProfileRequest, String.valueOf(httpServletRequest.getAttribute("email")));
    }


    @PostMapping("/verify-access-code")
    public String verifyAccessCode(@RequestBody @Valid LoginWithEmailRequest emailRequest) {
        return loginService.verifyAccessCode(emailRequest);
    }

}
