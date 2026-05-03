package com.brunoandradesa.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Resposta ao processo de autenticação contendo o token de acesso gerado")
public class TokenDTO {

  @NotBlank
  @Schema(
      description = "Token de acesso gerado",
      example = "td6Zhe9XDTA7dYWCSoCK4qPZ4A2oK7eUYEWch7v8jTttLA3DTpRKb9asiR6G3FM9")
  private String token;
}
