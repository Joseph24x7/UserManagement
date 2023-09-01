package com.user.mgmt.filter;

import com.user.mgmt.entity.UserEntity;
import com.user.mgmt.exception.ErrorEnums;
import com.user.mgmt.exception.UnAuthorizedException;
import com.user.mgmt.repository.UserInfoRepository;
import com.user.mgmt.service.GoogleAuthService;
import com.user.mgmt.util.TokenUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final InMemoryUserDetailsManager userDetailsManager;
    private final GoogleAuthService googleAuthService;
    private final TokenUtil tokenUtil;
    private final UserInfoRepository userInfoRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (isPreflightRequest(request) || isAccessCodeRequest(request)) {
            handleSpecialRequests(request);
        } else {
            handleAuthenticatedRequests(request);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPreflightRequest(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    private boolean isAccessCodeRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("/login-with-access-code") || uri.contains("/verify-access-code") || uri.contains("/redis");
    }

    private void handleSpecialRequests(HttpServletRequest request) {
        UserDetails userDetails = userDetailsManager.loadUserByUsername("username");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        authenticateAndSetContext(request, authenticationToken);
    }

    private void handleAuthenticatedRequests(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(accessToken)) {
            throw new UnAuthorizedException(ErrorEnums.AUTHORIZATION_REQUIRED);
        }

        UserEntity userEntity = getUserEntityFromToken(accessToken, request);
        authenticateAndSetContext(request, new UsernamePasswordAuthenticationToken(userEntity, userEntity.getPassword(), userEntity.getAuthorities()));
    }

    private UserEntity getUserEntityFromToken(String accessToken, HttpServletRequest request) {
        UserEntity userEntity;
        request.setAttribute("role", request.getHeader("X-BookMyGift-Role"));
        try {
            userEntity = googleAuthService.getUserInfo(accessToken);
            if (userEntity != null && StringUtils.isNotBlank(userEntity.getEmail())) {
                request.setAttribute("email", userEntity.getEmail());
            } else {
                throw new UnAuthorizedException(ErrorEnums.AUTHORIZATION_FAILED);
            }
        } catch (Exception e) {
            String email = tokenUtil.extractUsername(accessToken);
            if (StringUtils.isNotBlank(email)) {
                request.setAttribute("email", email);
                userEntity = userInfoRepository.findByEmail(email).orElseThrow(() -> new UnAuthorizedException(ErrorEnums.AUTHORIZATION_FAILED));
            } else {
                throw new UnAuthorizedException(ErrorEnums.AUTHORIZATION_FAILED);
            }
        }
        return userEntity;
    }

    private void authenticateAndSetContext(HttpServletRequest request, UsernamePasswordAuthenticationToken authenticationToken) {
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
