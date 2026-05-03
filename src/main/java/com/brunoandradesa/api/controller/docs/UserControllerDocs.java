package com.brunoandradesa.api.controller.docs;

import com.brunoandradesa.api.dto.request.UserCreateDTO;
import com.brunoandradesa.api.dto.request.UserFilterDTO;
import com.brunoandradesa.api.dto.request.UserUpdateDTO;
import com.brunoandradesa.api.dto.response.UserDTO;
import com.brunoandradesa.api.shared.docs.DefaultApiErrors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Users", description = "Métodos de manipulação de usuário")
public interface UserControllerDocs {

  @Operation(
      summary = "Obtém uma lista de usuários através dos critérios de pesquisa",
      description =
          "Rota responsável por retornar uma lista de usuários que atendam ao critério de pesquisa")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))))
  @DefaultApiErrors
  public List<UserDTO> getUsers(@ParameterObject UserFilterDTO filter);

  @Operation(
      summary = "Obtém um usuário pelo ID informado",
      description = "Rota responsável por obter um usuário através do ID")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UserDTO.class)))
  @DefaultApiErrors
  public UserDTO getUserById(
      @Parameter(
              description = "ID do usuário que se deseja localizar",
              required = true,
              example = "1",
              schema = @Schema(type = "integer", format = "int64"))
          @PathVariable
          Long id);

  @Operation(
      summary = "Cria um novo usuário",
      description = "Rota responsável por criar um novo usuário")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UserDTO.class)))
  @DefaultApiErrors
  public UserDTO createUser(@RequestBody @Valid UserCreateDTO request);

  @Operation(
      summary = "Atualiza os dados de um usuário",
      description = "Rota responsável atualizar os dados de um usuário")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UserDTO.class)))
  @DefaultApiErrors
  public UserDTO updateUser(
      @Parameter(
              description = "ID do usuário que será alterado",
              required = true,
              example = "1",
              schema = @Schema(type = "integer", format = "int64"))
          @PathVariable
          Long id,
      @RequestBody @Valid UserUpdateDTO request);

  @Operation(summary = "Habilita um usuário", description = "Rota responsável habilitar um usuário")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UserDTO.class)))
  @DefaultApiErrors
  public UserDTO enableUser(
      @Parameter(
              description = "ID do usuário que será habilitado",
              required = true,
              example = "1",
              schema = @Schema(type = "integer", format = "int64"))
          @PathVariable
          Long id);

  @Operation(
      summary = "Desabilita um usuário",
      description = "Rota responsável desabilitar um usuário")
  @ApiResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = UserDTO.class)))
  @DefaultApiErrors
  public UserDTO disableUser(
      @Parameter(
              description = "ID do usuário que será desabilitado",
              required = true,
              example = "1",
              schema = @Schema(type = "integer", format = "int64"))
          @PathVariable
          Long id);

  @Operation(
      summary = "Remove um usuário",
      description = "Rota responsável por remover um usuário existente")
  @ApiResponse(responseCode = "204", description = "No-Content")
  @DefaultApiErrors
  public void deleteUser(
      @Parameter(
              description = "ID do usuário que será excluído",
              required = true,
              example = "1",
              schema = @Schema(type = "integer", format = "int64"))
          @PathVariable
          Long id);
}
