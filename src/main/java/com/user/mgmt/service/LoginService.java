package com.user.mgmt.service;

import com.user.mgmt.model.GoogleUserEntity;
import com.user.mgmt.repository.GoogleUserInfoRepository;
import com.user.mgmt.request.MyProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final GoogleUserInfoRepository googleUserInfoRepository;

    public GoogleUserEntity saveUserInfo(String accessToken, String actionType) {

        GoogleUserEntity userInfoFromGoogle = getUserInfo(accessToken);

        Optional<GoogleUserEntity> optionalGoogleUserEntity = googleUserInfoRepository.findByEmail(userInfoFromGoogle.getEmail());

        if(optionalGoogleUserEntity.isPresent()) {

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

    private GoogleUserEntity getUserInfo(String accessToken) {

        WebClient webClient = WebClient.create("https://www.googleapis.com/oauth2/v3/userinfo");

        Mono<GoogleUserEntity> userInfoMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("access_token", accessToken).build())
                .retrieve()
                .bodyToMono(GoogleUserEntity.class);

        return userInfoMono.block();
    }
}
