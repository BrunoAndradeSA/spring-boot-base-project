package com.brunoandradesa.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RequestLogsFilterDTO {

  @Schema(description = "Filtra os logs de requisição pelo método HTTP", example = "POST")
  private String method;

  @Schema(description = "Filtra os logs de requisição pela URL requisitada", example = "/api/users")
  private String path;

  @Schema(description = "Filtra os logs de requisição pelo status HTTP", example = "200")
  private String status;

  @Schema(
      description = "Filtra os logs de requisição pelo username do usuário autenticado",
      example = "bruno.almeida")
  private String username;
}
