package com.user.mgmt.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Please select the role")
    private String role;

    public LoginRequest(String email, String role) {
        this.email = email;
        this.role = role;
    }
}
