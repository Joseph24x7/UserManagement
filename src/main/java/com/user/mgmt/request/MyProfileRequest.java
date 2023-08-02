package com.user.mgmt.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MyProfileRequest {
    private Long userId;
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;
    @Size(max = 10, message = "Username cannot be longer than 10 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username can only contain alphabets, underscores, and numbers")
    private String username;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot be longer than 100 characters")
    private String email;
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be exactly 10 numeric digits")
    private String mobile;
    private String gender;
}
