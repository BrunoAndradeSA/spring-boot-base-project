package com.brunoandradesa.api.security;

import com.brunoandradesa.api.shared.dto.DefaultResponseDTO;
import com.brunoandradesa.api.shared.util.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      org.springframework.security.access.AccessDeniedException ex)
      throws IOException, IOException {

    DefaultResponseDTO body =
        new DefaultResponseDTO(
            AppConstants.SC_GENERIC_ERROR, "Acesso negado", request.getRequestURI(), new Date());

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json");

    objectMapper.writeValue(response.getOutputStream(), body);
  }
}
