package com.brunoandradesa.api.controller;

import com.brunoandradesa.api.controller.docs.HealthControllerDocs;
import com.brunoandradesa.api.shared.dto.DefaultResponseDTO;
import com.brunoandradesa.api.shared.util.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController implements HealthControllerDocs {

  @Override
  @GetMapping
  public ResponseEntity<DefaultResponseDTO> healthCheck(HttpServletRequest request) {
    DefaultResponseDTO resp;
    String uri = request.getRequestURI();
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    try {
      httpStatus = HttpStatus.OK;

      resp = new DefaultResponseDTO(AppConstants.SC_OK, "Serviço em operação", uri, new Date());
    } catch (Exception e) {
      resp =
          new DefaultResponseDTO(
              AppConstants.SC_GENERIC_ERROR,
              "Erro ao verificar os status de operação do servico: %s".formatted(e.getMessage()),
              uri,
              new Date());
    }

    return new ResponseEntity<>(resp, httpStatus);
  }
}
