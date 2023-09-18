package com.user.mgmt.config;

import com.user.mgmt.entity.UserEntity;
import com.user.mgmt.exception.ErrorEnums;
import com.user.mgmt.exception.UnAuthorizedException;
import com.user.mgmt.repository.UserInfoRepository;
import com.user.mgmt.client.GoogleAuthClient;
import com.user.mgmt.util.TokenUtil;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RequestHeaderAuthenticationProvider implements AuthenticationProvider {

    private final GoogleAuthClient googleAuthClient;
    private final TokenUtil tokenUtil;
    private final UserInfoRepository userInfoRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String accessToken = String.valueOf(authentication.getPrincipal());
        UserEntity userEntity;
        try {
            userEntity = googleAuthClient.getUserInfo(accessToken);
            if (userEntity == null || StringUtils.isBlank(userEntity.getEmail())) {
                throw new UnAuthorizedException(ErrorEnums.AUTHORIZATION_FAILED);
            }
        } catch (Exception e) {
            String email = tokenUtil.extractUsername(accessToken);
            if (StringUtils.isNotBlank(email)) {
                userEntity = userInfoRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("Bad Request Header Credentials"));
            } else {
                throw new UnAuthorizedException(ErrorEnums.AUTHORIZATION_FAILED);
            }
        }

        return new PreAuthenticatedAuthenticationToken(authentication.getPrincipal(), userEntity.getEmail(), new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}