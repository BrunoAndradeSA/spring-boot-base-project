package com.brunoandradesa.api.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

@Data
@Schema(description = "Modelo de resposta padrão para as rotas")
public class DefaultResponseDTO {

  @Schema(description = "Código de status de resposta", example = "200")
  private final int status;

  @Schema(description = "Mensagem da resposta", example = "OK")
  private final String message;

  @Schema(description = "Detalhes adicionais da mensagem de resposta", example = "/api/health")
  private final String details;

  @Schema(description = "Timestamp de retorno da mensagem", example = "2026-04-13 21:04:44")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private final Date timestamp;
}
