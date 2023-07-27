package com.user.mgmt.config;

import com.user.mgmt.exception.ErrorEnums;
import com.user.mgmt.exception.UnAuthorizedException;
import com.user.mgmt.model.GoogleUserEntity;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class CustomAuthenticatedInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true;
        }

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

        return true;
    }

    private GoogleUserEntity getUserInfo(String accessToken) {

        WebClient webClient = WebClient.create("https://www.googleapis.com/oauth2/v3/userinfo");

        Mono<GoogleUserEntity> userInfoMono = webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("access_token", accessToken).build())
                .retrieve()
                .bodyToMono(GoogleUserEntity.class);

        return userInfoMono.block();
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) {
        // Do nothing in this method
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        // Do nothing in this method
    }
}