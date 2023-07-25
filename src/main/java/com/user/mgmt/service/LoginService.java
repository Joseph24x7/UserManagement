package com.user.mgmt.service;

import com.user.mgmt.model.GoogleUserEntity;
import com.user.mgmt.repository.GoogleUserInfoRepository;
import com.user.mgmt.request.MyProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final GoogleUserInfoRepository googleUserInfoRepository;

    public GoogleUserEntity saveUserInfo(String email) {

        Optional<GoogleUserEntity> optionalGoogleUserEntity = googleUserInfoRepository.findByEmail(email);

        if (optionalGoogleUserEntity.isPresent()) {

            GoogleUserEntity userEntity = optionalGoogleUserEntity.get();

            return googleUserInfoRepository.save(userEntity);

        }

        return null;
    }

    public GoogleUserEntity updateUserInfo(MyProfileRequest myProfileRequest) {

        Optional<GoogleUserEntity> optionalGoogleUserEntity = googleUserInfoRepository.findByEmail(myProfileRequest.getEmail());

        if(optionalGoogleUserEntity.isPresent()) {

            GoogleUserEntity userEntity = optionalGoogleUserEntity.get();
            userEntity.setName(myProfileRequest.getName());
            userEntity.setUsername(myProfileRequest.getUsername());
            userEntity.setMobile(myProfileRequest.getMobile());
            userEntity.setGender(myProfileRequest.getGender());

        }
        return googleUserInfoRepository.save(optionalGoogleUserEntity.get());

    }

}
