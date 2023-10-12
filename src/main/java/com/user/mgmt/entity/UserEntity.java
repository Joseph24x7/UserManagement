package com.user.mgmt.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "USER_INFO", schema = "myapp")
public class UserEntity implements Serializable {

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

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name = "registered_on")
    @CreationTimestamp
    private LocalDateTime registeredOn;

    @Column(name = "recent_login")
    @UpdateTimestamp
    private LocalDateTime recentLogin;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "gender")
    private String gender;

    @Column(name = "otp")
    private Integer otp;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId")
    private AddressEntity address;

}
