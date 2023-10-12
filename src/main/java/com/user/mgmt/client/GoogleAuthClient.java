package com.user.mgmt.client;

import com.user.mgmt.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GoogleAuthClient {

    private final WebClient webClient = WebClient.create("https://www.googleapis.com/oauth2/v3/userinfo");

    public UserEntity getUserInfo(String accessToken) {

        Mono<UserEntity> userInfoMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("access_token", accessToken).build())
                .retrieve()
                .bodyToMono(UserEntity.class);

        return userInfoMono.block();
    }

}
