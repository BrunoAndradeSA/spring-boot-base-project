package com.brunoandradesa.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
public class UserFilterDTO {
  @Schema(description = "Filtra por username (busca parcial)", example = "bruno")
  private String username;

  @Schema(description = "Filtra usuários habilitados/desabilitados", example = "true")
  private Boolean enabled;

  @Schema(
      description = "Lista de roles (usuário deve possuir pelo menos uma)",
      example = "[\"ROLE_ADMIN\", \"ROLE_USER\"]")
  private List<String> roles;
}
