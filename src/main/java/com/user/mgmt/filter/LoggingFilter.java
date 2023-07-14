package com.user.mgmt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Qualifier("logFilter")
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String correlationId = generateCorrelationId();

        MDC.put("CorrelationId", correlationId);

        log.info("CorrelationId: {}, Request details: Method: {}, URL: {}, Content-Type: {}, Accept: {}",
                correlationId, request.getMethod(), request.getRequestURL(), request.getContentType(), request.getHeader(HttpHeaders.ACCEPT));

        filterChain.doFilter(request, response);

        log.info("CorrelationId: {}, Responded with {} status", correlationId, response.getStatus());

        MDC.remove ("CorrelationId");
    }

    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
