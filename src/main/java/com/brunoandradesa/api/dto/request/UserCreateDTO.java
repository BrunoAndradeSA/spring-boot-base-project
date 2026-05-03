package com.brunoandradesa.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO utilizado para criação de um novo usuário")
public class UserCreateDTO {

  @NotBlank(message = "username é obrigatório")
  @Size(min = 3, max = 50, message = "username deve ter entre 3 e 50 caracteres")
  @Schema(
      description = "Nome de usuário único utilizado para login",
      example = "bruno.andrade",
      minLength = 3,
      maxLength = 50,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String username;

  @NotBlank(message = "password é obrigatória")
  @Size(min = 6, max = 100, message = "password deve ter entre 6 e 100 caracteres")
  @Schema(
      description = "Senha do usuário (será criptografada antes de persistir)",
      example = "Senha@123",
      minLength = 6,
      maxLength = 100,
      requiredMode = Schema.RequiredMode.REQUIRED,
      format = "password")
  private String password;

  @Schema(
      description = "Lista de perfis (roles) atribuídos ao usuário",
      example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
  private Set<String> roles;
}
