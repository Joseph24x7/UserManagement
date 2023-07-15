package com.user.mgmt.repository;

import com.user.mgmt.model.GoogleUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleUserInfoRepository extends JpaRepository<GoogleUserEntity, Long> {
}
