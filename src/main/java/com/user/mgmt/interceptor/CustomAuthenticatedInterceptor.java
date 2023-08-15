/*
package com.user.mgmt.interceptor;

import com.user.mgmt.exception.ErrorEnums;
import com.user.mgmt.exception.UnAuthorizedException;
import com.user.mgmt.entity.UserEntity;
import com.user.mgmt.service.GoogleAuthService;
import com.user.mgmt.util.TokenUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

*/
/*@Component
@RequiredArgsConstructor
public class CustomAuthenticatedInterceptor implements HandlerInterceptor {

    private final GoogleAuthService googleAuthService;
    private final TokenUtil tokenUtil;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true;
        } else if (request.getRequestURI().contains("/login-with-access-code") ||
                request.getRequestURI().contains("/verify-access-code")) {
            return true;
        }

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (StringUtils.isNotBlank(accessToken)) {
                try {
                    UserEntity userInfoFromGoogle = googleAuthService.getUserInfo(accessToken);
                    if (Objects.nonNull(userInfoFromGoogle) && StringUtils.isNotBlank(userInfoFromGoogle.getEmail())) {
                        request.setAttribute("email", userInfoFromGoogle.getEmail());
                    } else {
                        throw new UnAuthorizedException(ErrorEnums.AUTHORIZATION_FAILED);
                    }
                } catch (Exception e) {
                    String email = tokenUtil.extractUsername(accessToken);
                    if (StringUtils.isNotBlank(email)) {
                        request.setAttribute("email", email);
                    } else {
                        throw new UnAuthorizedException(ErrorEnums.AUTHORIZATION_FAILED);
                    }
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

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) {
        // Do nothing in this method
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        // Do nothing in this method
    }
}*/
