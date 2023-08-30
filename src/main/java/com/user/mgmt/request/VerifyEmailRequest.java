package com.user.mgmt.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyEmailRequest {

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "AccessCode should not be empty")
    private String accessCode;

}
