package com.user.mgmt.repository;

import com.user.mgmt.model.GoogleUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleUserInfoRepository extends JpaRepository<GoogleUserEntity, Long> {

    Optional<GoogleUserEntity> findByEmail(String email);

}

