package com.brunoandradesa.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta contendo os logs de requisição de uma data específica")
public class RequestLogsDTO {

  @Schema(
      description = "Data/Hora de registro do log de requisição",
      example = "2026-04-10T14:20:00Z")
  private LocalDateTime createdAt;

  @Schema(description = "Método HTTP da requisição", example = "GET")
  private String method;

  @Schema(description = "Rota da API que foi requisitada", example = "/api/users")
  private String path;

  @Schema(description = "Status code HTTP da requisição", example = "200")
  private Integer status;

  @Schema(
      description = "Username do usuário autenticado que disparou a requisição",
      example = "bruno.almeida")
  private String username;

  @Schema(description = "Tempo total da requisição (em millisegundos)", example = "10")
  private Long duration;

  @Schema(description = "Headers da requisição")
  private String headers;

  @Schema(description = "Corpo da requisição no formato JSON Text")
  private String requestBody;

  @Schema(description = "Resposta no formato JSON Text")
  private String responseBody;

  @Schema(description = "Requisição completa no formato cURL")
  private String curl;
}
