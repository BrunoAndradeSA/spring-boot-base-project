package com.brunoandradesa.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.brunoandradesa.api.domain.auth.AuthService;
import com.brunoandradesa.api.dto.request.LoginRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class AuthControllerTest {

  private MockMvc mockMvc;

  @Mock private AuthService authService;

  @InjectMocks private AuthController authController;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    objectMapper = new ObjectMapper();
  }

  @Nested
  @DisplayName("POST /auth/login")
  class LoginTests {

    @Test
    @DisplayName("should return token when credentials are valid")
    void shouldReturnTokenWhenCredentialsAreValid() throws Exception {
      LoginRequestDTO request = new LoginRequestDTO();
      request.setUsername("testuser");
      request.setPassword("password");

      when(authService.login("testuser", "password")).thenReturn("jwt-token");

      mockMvc
          .perform(
              post("/auth/login")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    @DisplayName("should return 400 when username is blank")
    void shouldReturn400WhenUsernameIsBlank() throws Exception {
      LoginRequestDTO request = new LoginRequestDTO();
      request.setUsername("");
      request.setPassword("password");

      mockMvc
          .perform(
              post("/auth/login")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when password is blank")
    void shouldReturn400WhenPasswordIsBlank() throws Exception {
      LoginRequestDTO request = new LoginRequestDTO();
      request.setUsername("testuser");
      request.setPassword("");

      mockMvc
          .perform(
              post("/auth/login")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("POST /auth/client")
  class ClientTests {

    @Test
    @DisplayName("should return token when client credentials are valid")
    void shouldReturnTokenWhenClientCredentialsAreValid() throws Exception {
      String basicAuth = "Basic " + java.util.Base64.getEncoder().encodeToString(
          "client-id:client-secret".getBytes());

      when(authService.clientLogin("client-id", "client-secret")).thenReturn("client-jwt-token");

      mockMvc
          .perform(
              post("/auth/client")
                  .header("Authorization", basicAuth))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.token").value("client-jwt-token"));
    }

    @Test
    @DisplayName("should return 400 when Authorization header is missing")
    void shouldReturn400WhenAuthorizationHeaderIsMissing() throws Exception {
      mockMvc
          .perform(post("/auth/client"))
          .andExpect(status().isBadRequest());
    }
  }
}