package com.user.mgmt.controller;

import com.user.mgmt.model.GoogleUserEntity;
import com.user.mgmt.request.GoogleOAuth2Request;
import com.user.mgmt.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/user-info")
    public GoogleUserEntity saveUserInfo(@RequestBody GoogleOAuth2Request googleOAuth2Request) {
        return loginService.saveUserInfo(googleOAuth2Request.getAccess_token());
    }

}

