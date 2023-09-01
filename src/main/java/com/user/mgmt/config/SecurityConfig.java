package com.user.mgmt.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RequestHeaderAuthenticationProvider requestHeaderAuthenticationProvider;
    private final DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(requestHeaderAuthenticationFilter(), HeaderWriterFilter.class)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers(HttpMethod.OPTIONS).permitAll();
                    request.requestMatchers("/login-with-access-code", "/verify-access-code", "/redis/**").permitAll();
                    request.anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex.authenticationEntryPoint(delegatedAuthenticationEntryPoint))
                .cors(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() {
        RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
        filter.setPrincipalRequestHeader(HttpHeaders.AUTHORIZATION);
        filter.setExceptionIfHeaderMissing(false);
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/**"));
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    protected AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(requestHeaderAuthenticationProvider));
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

@Component
class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public DelegatedAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        resolver.resolveException(request, response, null, authException);
    }

}
