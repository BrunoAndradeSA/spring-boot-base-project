package com.brunoandradesa.api.controller.docs;

import com.brunoandradesa.api.shared.dto.DefaultResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Health Check", description = "Verificação de status online da API")
public interface HealthControllerDocs {

  @Operation(
      summary = "Verifica se a API está online",
      description = "Endpoint simples para validação de disponibilidade da API")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Internal Server Error",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  public ResponseEntity<DefaultResponseDTO> healthCheck(HttpServletRequest request);
}
