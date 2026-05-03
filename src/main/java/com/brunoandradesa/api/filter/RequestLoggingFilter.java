package com.brunoandradesa.api.filter;

import com.brunoandradesa.api.domain.logs.LogsService;
import com.brunoandradesa.api.shared.util.AppConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

  private final LogsService logsService;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();

    return AppConstants.REQUEST_LOGS_EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    long start = System.currentTimeMillis();

    ContentCachingRequestWrapper reqWrapper =
        new ContentCachingRequestWrapper(request, 1024 * 1024);
    ContentCachingResponseWrapper resWrapper = new ContentCachingResponseWrapper(response);

    try {
      filterChain.doFilter(reqWrapper, resWrapper);
    } finally {
      long duration = System.currentTimeMillis() - start;

      String requestBody = "";
      String responseBody = "";

      if (isTextContent(request.getContentType())) {
        requestBody = new String(reqWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
      }

      if (isTextContent(response.getContentType())) {
        responseBody = new String(resWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
      }

      logsService.saveRequestLog(reqWrapper, resWrapper, requestBody, responseBody, duration);

      resWrapper.copyBodyToResponse();
    }
  }

  private boolean isTextContent(String contentType) {
    if (contentType == null) return false;

    return contentType.contains("application/json")
        || contentType.contains("application/xml")
        || contentType.contains("text/")
        || contentType.contains("application/x-www-form-urlencoded");
  }
}
