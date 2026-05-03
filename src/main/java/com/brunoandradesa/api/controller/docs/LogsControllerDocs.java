package com.brunoandradesa.api.controller.docs;

import com.brunoandradesa.api.dto.request.RequestLogsFilterDTO;
import com.brunoandradesa.api.dto.response.LogsDTO;
import com.brunoandradesa.api.dto.response.RequestLogsDTO;
import com.brunoandradesa.api.shared.docs.DefaultApiErrors;
import com.brunoandradesa.api.shared.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Logs", description = "Pesquisa de logs da API")
public interface LogsControllerDocs {

  @Operation(
      summary = "Lista os logs da aplicação",
      description = "Lista os logs da aplicação filtrando pela data de ocorrência do log")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = LogsDTO.class)))
  @DefaultApiErrors
  public ResponseEntity<LogsDTO> getLogsByDate(
      @Parameter(
              description = "Data de pesquisa dos logs no formato YYYY-MM-DD",
              required = true,
              example = "2026-04-20",
              schema = @Schema(type = "String"))
          @PathVariable
          @Pattern(regexp = AppConstants.REGEXP_DATE)
          String date,
      @Parameter(
              description = "Log level a ser pesquisado",
              required = false,
              example = "WARN",
              schema = @Schema(type = "String"))
          @RequestParam(required = false)
          String level);

  @Operation(
      summary = "Lista os logs de requisição da aplicação",
      description =
          "Lista os logs de requisição da aplicação filtrando pela data de ocorrência do log")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(array = @ArraySchema(schema = @Schema(implementation = RequestLogsDTO.class))))
  @DefaultApiErrors
  public List<RequestLogsDTO> getRequestLogsByDate(
      @Parameter(
              description = "Data de pesquisa dos logs de requisição no formato YYYY-MM-DD",
              required = true,
              example = "2026-04-20",
              schema = @Schema(type = "String"))
          @PathVariable
          @Pattern(regexp = AppConstants.REGEXP_DATE)
          String date,
      @ParameterObject RequestLogsFilterDTO filter);
}
