package com.user.mgmt.config;

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
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final InMemoryUserDetailsManager userDetailsManager;
    private final GoogleAuthService googleAuthService;
    private final TokenUtil tokenUtil;
    private final UserInfoRepository userInfoRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        UserDetails userDetails;
        UsernamePasswordAuthenticationToken authenticationToken;

        if (request.getMethod().equalsIgnoreCase("OPTIONS") || (request.getRequestURI().contains("/login-with-access-code") || request.getRequestURI().contains("/verify-access-code")) || request.getRequestURI().contains("/redis")) {
            userDetails = userDetailsManager.loadUserByUsername("username");
            authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        } else {

            String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            UserEntity userEntity;
            if (StringUtils.isNotBlank(accessToken)) {
                try {
                    userEntity = googleAuthService.getUserInfo(accessToken);
                    if (Objects.nonNull(userEntity) && StringUtils.isNotBlank(userEntity.getEmail())) {
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
            } else {
                throw new UnAuthorizedException(ErrorEnums.AUTHORIZATION_REQUIRED);
            }

            authenticationToken = new UsernamePasswordAuthenticationToken(userEntity, userEntity.getPassword(), userEntity.getAuthorities());
        }

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }

}
