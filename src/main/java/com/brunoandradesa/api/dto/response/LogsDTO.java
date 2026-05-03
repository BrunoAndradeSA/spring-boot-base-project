package com.brunoandradesa.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
@Schema(description = "Resposta contendo os logs de uma data específica")
public class LogsDTO {

  @Schema(description = "Data dos logs no formato yyyy-MM-dd", example = "2026-04-17")
  private String date;

  @Schema(description = "Quantidade total de logs retornados", example = "120")
  private int total;

  @Schema(
      description =
          "Lista de logs no formato JSON (cada item representa uma linha do arquivo de log)",
      example =
          """
          [
            {
              "@timestamp": "2026-04-17T22:22:17",
              "level": "INFO",
              "message": "Started application",
              "traceId": "abc-123"
            }
          ]
          """)
  private List<Map<String, Object>> logs;

  public LogsDTO(String date, List<Map<String, Object>> logs) {
    this.date = date;
    this.logs = logs;
    this.total = logs.size();
  }
}
