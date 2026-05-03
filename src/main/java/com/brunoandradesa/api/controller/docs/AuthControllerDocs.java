package com.brunoandradesa.api.controller.docs;

import com.brunoandradesa.api.dto.request.LoginRequestDTO;
import com.brunoandradesa.api.dto.response.TokenDTO;
import com.brunoandradesa.api.shared.dto.DefaultResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Authentication", description = "Métodos de autenticação da API")
public interface AuthControllerDocs {

  @Operation(
      summary = "Obtém um token de autenticação por usuário e senha",
      description =
          "Rota responsável por gerar um token de autenticação através do usuário e senha"
              + " informados")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = TokenDTO.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Bad Request",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  @ApiResponse(
      responseCode = "401",
      description = "Unauthorized",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  @ApiResponse(
      responseCode = "403",
      description = "Forbidden",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Not Found",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  @ApiResponse(
      responseCode = "405",
      description = "Method Not Allowed",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Internal Server Error",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  public TokenDTO login(@RequestBody @Valid LoginRequestDTO request);

  @Operation(
      summary = "Obtém um token de autenticação por client ID e Secret",
      description =
          "Rota responsável por gerar um token de autenticação através do client ID e Secret"
              + " informados")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = TokenDTO.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Bad Request",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  @ApiResponse(
      responseCode = "401",
      description = "Unauthorized",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  @ApiResponse(
      responseCode = "403",
      description = "Forbidden",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Not Found",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  @ApiResponse(
      responseCode = "405",
      description = "Method Not Allowed",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Internal Server Error",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DefaultResponseDTO.class)))
  public TokenDTO client(@RequestHeader("Authorization") String header);
}
