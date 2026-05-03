package com.brunoandradesa.api.security;

import com.brunoandradesa.api.shared.dto.DefaultResponseDTO;
import com.brunoandradesa.api.shared.util.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
      throws IOException {

    DefaultResponseDTO body =
        new DefaultResponseDTO(
            AppConstants.SC_GENERIC_ERROR, "Não autenticado", request.getRequestURI(), new Date());

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    objectMapper.writeValue(response.getOutputStream(), body);
  }
}
