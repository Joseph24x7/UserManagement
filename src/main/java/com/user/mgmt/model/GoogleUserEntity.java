package com.user.mgmt.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "GOOGLE_USER_INFO", schema = "myapp")
public class GoogleUserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "registered_on")
    private LocalDateTime registeredOn;

    @Column(name = "recent_login")
    private LocalDateTime recentLogin;

}
