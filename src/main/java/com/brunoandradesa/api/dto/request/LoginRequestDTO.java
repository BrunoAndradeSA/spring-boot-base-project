package com.brunoandradesa.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Payload de requisição de um token de autenticação por usuário e senha")
public class LoginRequestDTO {

  @NotBlank
  @Schema(description = "Identificação do usuário", example = "user")
  private String username;

  @NotBlank
  @Schema(description = "Senha do usuário", example = "user")
  private String password;
}
