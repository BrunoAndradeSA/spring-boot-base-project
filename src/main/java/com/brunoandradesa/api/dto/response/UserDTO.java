package com.brunoandradesa.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa os dados de um usuário retornados pela API")
public class UserDTO {

  @Schema(description = "ID único do usuário", example = "1")
  private Long id;

  @Schema(description = "Nome de usuário (login)", example = "bruno.andrade")
  private String username;

  @Schema(description = "Indica se o usuário está ativo", example = "true")
  private boolean enabled;

  @Schema(description = "Indica se a conta não está bloqueada", example = "true")
  private boolean accountNonLocked;

  @Schema(description = "Quantidade de tentativas de login falhadas", example = "0")
  private int failedAttempts;

  @Schema(description = "Data do último login", example = "2026-04-18T20:30:00Z")
  private Date lastLogin;

  @Schema(description = "Data de criação do usuário", example = "2026-04-01T10:15:30Z")
  private Date createdAt;

  @Schema(description = "Data da última atualização do usuário", example = "2026-04-10T14:20:00Z")
  private Date updatedAt;

  @Schema(
      description = "Lista de perfis (roles) do usuário",
      example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
  private Set<String> roles;
}
