package com.user.mgmt.service;

import com.user.mgmt.model.GoogleUserEntity;
import com.user.mgmt.repository.GoogleUserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final GoogleUserInfoRepository googleUserInfoRepository;

    public GoogleUserEntity saveUserInfo(String accessToken) {

        GoogleUserEntity userInfo = getUserInfo(accessToken);

        return googleUserInfoRepository.save(userInfo);

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
