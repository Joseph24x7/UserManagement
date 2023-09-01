package com.user.mgmt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        MyHttpServletRequestWrapper requestWrapper = new MyHttpServletRequestWrapper(request);
        MyHttpServletResponseWrapper responseWrapper = new MyHttpServletResponseWrapper(response);

        log.info("Request headers: {}", requestWrapper.getHeadersAsMap());
        log.info("Request payload: {}", new String(requestWrapper.getBody(), StandardCharsets.UTF_8));

        filterChain.doFilter(requestWrapper, responseWrapper);

        log.info("Response status: {}", responseWrapper.getStatus());
        log.info("Response payload: {}", new String(responseWrapper.getCapturedResponseData(), StandardCharsets.UTF_8));

    }

}

@Slf4j
@Getter
class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    public MyHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            body = IOUtils.toByteArray(request.getInputStream());
        } catch (IOException ex) {
            body = new byte[0];
            log.warn("IOException occurred at ", ex);
        }
    }

    public Map<String, String> getHeadersAsMap() {
        Map<String, String> headersMap = new HashMap<>();
        Enumeration<String> headerNames = getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headersMap.put(headerName, getHeader(headerName));
        }
        return headersMap;
    }


    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                throw new UnsupportedOperationException();
            }
        };
    }

}

@Slf4j
class MyHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final CharArrayWriter charArrayWriter = new CharArrayWriter();
    private final PrintWriter writer = new PrintWriter(charArrayWriter);

    public MyHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    @Override
    public void setContentLength(int len) {
        // Ignore setContentLength to prevent response truncation
    }

    @Override
    public void setContentLengthLong(long len) {
        // Ignore setContentLength to prevent response truncation
    }

    public byte[] getCapturedResponseData() {
        writer.flush();
        return charArrayWriter.toString().getBytes(StandardCharsets.UTF_8);
    }
}