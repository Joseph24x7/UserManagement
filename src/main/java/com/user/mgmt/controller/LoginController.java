package com.user.mgmt.controller;

import com.user.mgmt.model.GoogleUserEntity;
import com.user.mgmt.request.MyProfileRequest;
import com.user.mgmt.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/user-info")
    public GoogleUserEntity saveUserInfo(HttpServletRequest httpServletRequest) {
        String email = (String) httpServletRequest.getAttribute("email");
        return loginService.saveUserInfo(httpServletRequest.getHeader("Authorization"), httpServletRequest.getHeader("X-Action-Type"));
    }

    @PutMapping("/update-user-info")
    public GoogleUserEntity saveUserInfo(@RequestBody MyProfileRequest myProfileRequest) {
        return loginService.updateUserInfo(myProfileRequest);
    }

}


