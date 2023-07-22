package com.user.mgmt.request;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyProfileRequest {
    private Long userId;
    private String name;
    private String username;
    private String email;
    private String mobile;
    private String gender;
    private GoogleOAuth2Request tokenResponse;
}
