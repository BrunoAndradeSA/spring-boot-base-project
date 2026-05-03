package com.brunoandradesa.api.shared.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TraceIdFilter extends OncePerRequestFilter {

  private static final String TRACE_ID = "traceId";
  private static final String HEADER_NAME = "X-Trace-Id";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String traceId = request.getHeader(HEADER_NAME);

      if (traceId == null || traceId.isBlank()) {
        traceId = UUID.randomUUID().toString();
      }

      MDC.put(TRACE_ID, traceId);

      response.setHeader(HEADER_NAME, traceId);

      filterChain.doFilter(request, response);

    } finally {
      MDC.clear();
    }
  }
}
