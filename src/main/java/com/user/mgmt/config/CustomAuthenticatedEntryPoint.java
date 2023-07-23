package com.user.mgmt.config;

import com.user.mgmt.exception.ErrorEnums;
import com.user.mgmt.exception.UnAuthorizedException;
import com.user.mgmt.model.GoogleUserEntity;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class CustomAuthenticatedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (StringUtils.isNotBlank(accessToken)) {
                GoogleUserEntity userInfoFromGoogle = getUserInfo(accessToken);
                if (Objects.nonNull(userInfoFromGoogle) && StringUtils.isNotBlank(userInfoFromGoogle.getEmail())) {
                    request.setAttribute("email", userInfoFromGoogle.getEmail());
                } else {
                    throw new UnAuthorizedException(ErrorEnums.AUTHORIZATION_FAILED);
                }
            } else {
                throw new UnAuthorizedException(ErrorEnums.AUTHORIZATION_REQUIRED);
            }
        } catch (UnAuthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnAuthorizedException(ErrorEnums.AUTHORIZATION_FAILED);
        }
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