package com.user.mgmt.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LoginController {

    @PostMapping("/api/saveCredentials")
    public String redirectToLogin(@RequestBody UserDTO userDTO) {
        return "user-login-successful";
    }

}

