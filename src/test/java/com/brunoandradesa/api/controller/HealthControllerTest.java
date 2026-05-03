package com.brunoandradesa.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

  @Test
  @DisplayName("should return OK when service is healthy")
  void shouldReturnOkWhenServiceIsHealthy() throws Exception {
    HealthController controller = new HealthController();
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    mockMvc
        .perform(get("/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(0))
        .andExpect(jsonPath("$.message").value("Serviço em operação"))
        .andExpect(jsonPath("$.details").value("/health"))
        .andExpect(jsonPath("$.timestamp").exists());
  }
}