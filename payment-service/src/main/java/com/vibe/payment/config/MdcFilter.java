package com.vibe.payment.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class MdcFilter implements Filter {

    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String requestId = httpRequest.getHeader(REQUEST_ID_HEADER);

            if (requestId == null || requestId.isEmpty()) {
                requestId = UUID.randomUUID().toString(); // Generate if not present
            }

            MDC.put(REQUEST_ID_MDC_KEY, requestId);
            try {
                chain.doFilter(request, response);
            } finally {
                MDC.remove(REQUEST_ID_MDC_KEY);
            }
        }
}
