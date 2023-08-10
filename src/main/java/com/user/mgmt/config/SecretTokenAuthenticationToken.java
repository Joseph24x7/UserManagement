package com.user.mgmt.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SecretTokenAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    public SecretTokenAuthenticationToken(String token) {
        super(null);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}

