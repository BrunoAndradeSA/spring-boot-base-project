package com.brunoandradesa.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO utilizado para atualização de cadastro de usuários")
public class UserUpdateDTO {

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
