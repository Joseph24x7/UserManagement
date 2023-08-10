package com.user.mgmt.service;

import com.user.mgmt.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GoogleAuthService {

    public UserEntity getUserInfo(String accessToken) {

        WebClient webClient = WebClient.create("https://www.googleapis.com/oauth2/v3/userinfo");

        Mono<UserEntity> userInfoMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("access_token", accessToken).build())
                .retrieve()
                .bodyToMono(UserEntity.class);

        return userInfoMono.block();
    }

}
