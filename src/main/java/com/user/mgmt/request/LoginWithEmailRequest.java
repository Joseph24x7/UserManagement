package com.user.mgmt.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginWithEmailRequest {

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Invalid email format")
    private String email;

    private Integer accessCode;

}
