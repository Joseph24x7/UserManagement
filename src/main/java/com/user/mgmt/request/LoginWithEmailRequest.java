package com.user.mgmt.request;

import lombok.Data;

@Data
public class LoginWithEmailRequest {

    private String email;
    private Integer accessCode;

}
