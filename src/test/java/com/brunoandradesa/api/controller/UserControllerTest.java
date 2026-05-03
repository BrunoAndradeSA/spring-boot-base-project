package com.brunoandradesa.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.brunoandradesa.api.domain.user.UserService;
import com.brunoandradesa.api.dto.request.UserCreateDTO;
import com.brunoandradesa.api.dto.request.UserFilterDTO;
import com.brunoandradesa.api.dto.request.UserUpdateDTO;
import com.brunoandradesa.api.dto.response.UserDTO;
import com.brunoandradesa.api.shared.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  private MockMvc mockMvc;

  @Mock private UserService userService;

  @InjectMocks private UserController userController;

  private ObjectMapper objectMapper;
  private UserDTO userDTO;
  private UserCreateDTO userCreateDTO;
  private UserUpdateDTO userUpdateDTO;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    objectMapper = new ObjectMapper();

    userDTO = new UserDTO();
    userDTO.setId(1L);
    userDTO.setUsername("bruno.andrade");
    userDTO.setEnabled(true);
    userDTO.setAccountNonLocked(true);
    userDTO.setFailedAttempts(0);
    userDTO.setCreatedAt(new Date());
    userDTO.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));

    userCreateDTO = new UserCreateDTO();
    userCreateDTO.setUsername("new.user");
    userCreateDTO.setPassword("Password123");
    userCreateDTO.setRoles(Set.of("ROLE_USER"));

    userUpdateDTO = new UserUpdateDTO();
    userUpdateDTO.setPassword("NewPassword123");
  }

  @Nested
  @DisplayName("GET /users")
  class GetUsersTests {

    @Test
    @DisplayName("should return list of users when filter matches")
    void shouldReturnListOfUsersWhenFilterMatches() throws Exception {
      when(userService.get(any(UserFilterDTO.class))).thenReturn(List.of(userDTO));

      mockMvc
          .perform(get("/users").param("username", "bruno"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].username").value("bruno.andrade"))
          .andExpect(jsonPath("$[0].id").value(1));

      verify(userService).get(any(UserFilterDTO.class));
    }

    @Test
    @DisplayName("should return empty list when no users match filter")
    void shouldReturnEmptyListWhenNoUsersMatch() throws Exception {
      when(userService.get(any(UserFilterDTO.class))).thenReturn(List.of());

      mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());

      verify(userService).get(any(UserFilterDTO.class));
    }
  }

  @Nested
  @DisplayName("GET /users/{id}")
  class GetUserByIdTests {

    @Test
    @DisplayName("should return user when user exists")
    void shouldReturnUserWhenExists() throws Exception {
      when(userService.getById(1L)).thenReturn(userDTO);

      mockMvc
          .perform(get("/users/1"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.username").value("bruno.andrade"))
          .andExpect(jsonPath("$.id").value(1));

      verify(userService).getById(1L);
    }

    @Test
    @DisplayName("should return 404 when user does not exist")
    void shouldReturn404WhenUserNotFound() throws Exception {
      when(userService.getById(999L))
          .thenThrow(new NotFoundException("Usuário não localizado pelo ID 999"));

      mockMvc.perform(get("/users/999")).andExpect(status().isNotFound());

      verify(userService).getById(999L);
    }
  }

  @Nested
  @DisplayName("POST /users")
  class CreateUserTests {

    @Test
    @DisplayName("should create user when request is valid")
    void shouldCreateUserWhenRequestIsValid() throws Exception {
      when(userService.create(any(UserCreateDTO.class))).thenReturn(userDTO);

      mockMvc
          .perform(
              post("/users")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(userCreateDTO)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.username").value("bruno.andrade"))
          .andExpect(jsonPath("$.id").value(1));

      verify(userService).create(any(UserCreateDTO.class));
    }

    @Test
    @DisplayName("should return 400 when username is empty")
    void shouldReturn400WhenUsernameIsEmpty() throws Exception {
      userCreateDTO.setUsername("");

      mockMvc
          .perform(
              post("/users")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(userCreateDTO)))
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("PUT /users/{id}")
  class UpdateUserTests {

    @Test
    @DisplayName("should update user when request is valid")
    void shouldUpdateUserWhenRequestIsValid() throws Exception {
      when(userService.update(any(Long.class), any(UserUpdateDTO.class))).thenReturn(userDTO);

      mockMvc
          .perform(
              put("/users/1")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(userUpdateDTO)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.username").value("bruno.andrade"));

      verify(userService).update(any(Long.class), any(UserUpdateDTO.class));
    }

    @Test
    @DisplayName("should return 404 when user to update does not exist")
    void shouldReturn404WhenUserNotFound() throws Exception {
      when(userService.update(any(Long.class), any(UserUpdateDTO.class)))
          .thenThrow(new NotFoundException("Usuário não localizado pelo ID 999"));

      mockMvc
          .perform(
              put("/users/999")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(userUpdateDTO)))
          .andExpect(status().isNotFound());

      verify(userService).update(any(Long.class), any(UserUpdateDTO.class));
    }
  }

  @Nested
  @DisplayName("PUT /users/{id}/enable")
  class EnableUserTests {

    @Test
    @DisplayName("should enable user when user exists")
    void shouldEnableUserWhenExists() throws Exception {
      when(userService.changeUserStatus(1L, true)).thenReturn(userDTO);

      mockMvc.perform(put("/users/1/enable")).andExpect(status().isOk());

      verify(userService).changeUserStatus(1L, true);
    }

    @Test
    @DisplayName("should return 404 when user to enable does not exist")
    void shouldReturn404WhenUserNotFound() throws Exception {
      when(userService.changeUserStatus(999L, true))
          .thenThrow(new NotFoundException("Usuário não localizado pelo ID 999"));

      mockMvc.perform(put("/users/999/enable")).andExpect(status().isNotFound());

      verify(userService).changeUserStatus(999L, true);
    }
  }

  @Nested
  @DisplayName("PUT /users/{id}/disable")
  class DisableUserTests {

    @Test
    @DisplayName("should disable user when user exists")
    void shouldDisableUserWhenExists() throws Exception {
      userDTO.setEnabled(false);
      when(userService.changeUserStatus(1L, false)).thenReturn(userDTO);

      mockMvc.perform(put("/users/1/disable")).andExpect(status().isOk());

      verify(userService).changeUserStatus(1L, false);
    }

    @Test
    @DisplayName("should return 404 when user to disable does not exist")
    void shouldReturn404WhenUserNotFound() throws Exception {
      when(userService.changeUserStatus(999L, false))
          .thenThrow(new NotFoundException("Usuário não localizado pelo ID 999"));

      mockMvc.perform(put("/users/999/disable")).andExpect(status().isNotFound());

      verify(userService).changeUserStatus(999L, false);
    }
  }

  @Nested
  @DisplayName("DELETE /users/{id}")
  class DeleteUserTests {

    @Test
    @DisplayName("should delete user when user exists")
    void shouldDeleteUserWhenExists() throws Exception {
      doNothing().when(userService).delete(1L);

      mockMvc.perform(delete("/users/1")).andExpect(status().isOk());

      verify(userService).delete(1L);
    }

    @Test
    @DisplayName("should return 404 when user to delete does not exist")
    void shouldReturn404WhenUserNotFound() throws Exception {
      doThrow(new NotFoundException("Usuário não localizado pelo ID 999"))
          .when(userService).delete(999L);

      mockMvc.perform(delete("/users/999")).andExpect(status().isNotFound());

      verify(userService).delete(999L);
    }
  }
}