package ru.homework.provider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RequestLog extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestLog.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !"/rpc".equals(request.getRequestURI())
                && !"/rpc".equals(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        var input = new ContentCachingRequestWrapper(request, 4096);
        var output = new ContentCachingResponseWrapper(response);
        log.info("Request: {} /rpc client={}", request.getMethod(), request.getHeader("X-Client-Id"));
        try {
            chain.doFilter(input, output);
            log.info("Response: status={} body={}", output.getStatus(), text(output.getContentAsByteArray()));
        } catch (ServletException | IOException | RuntimeException e) {
            log.error("Request failed: {}", e.toString());
            throw e;
        } finally {
            log.info("Request body: {}", text(input.getContentAsByteArray()));
            output.copyBodyToResponse();
        }
    }

    private String text(byte[] bytes) {
        return new String(bytes, 0, Math.min(bytes.length, 4096), StandardCharsets.UTF_8)
                .replace('\n', ' ').replace('\r', ' ');
    }
}
