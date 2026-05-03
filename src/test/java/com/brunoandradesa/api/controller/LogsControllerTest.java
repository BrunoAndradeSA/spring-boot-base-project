package com.brunoandradesa.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.brunoandradesa.api.domain.logs.LogsService;
import com.brunoandradesa.api.dto.request.RequestLogsFilterDTO;
import com.brunoandradesa.api.dto.response.RequestLogsDTO;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class LogsControllerTest {

  private MockMvc mockMvc;

  @Mock private LogsService logsService;

  @InjectMocks private LogsController logsController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(logsController).build();
  }

  @Nested
  @DisplayName("GET /logs/{date}")
  class GetLogsByDateTests {

    @Test
    @DisplayName("should return logs when date is valid")
    void shouldReturnLogsWhenDateIsValid() throws Exception {
      List<Map<String, Object>> logs =
          List.of(
              Map.of("timestamp", "2026-05-03 10:00:00", "level", "INFO", "message", "Test log"));

      when(logsService.getLogsByDate(anyString(), any())).thenReturn(logs);

      mockMvc
          .perform(get("/logs/2026-05-03"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.date").value("2026-05-03"))
          .andExpect(jsonPath("$.logs").isArray())
          .andExpect(jsonPath("$.logs[0].level").value("INFO"));
    }

    @Test
    @DisplayName("should return empty logs list when no logs found")
    void shouldReturnEmptyLogsListWhenNoLogsFound() throws Exception {
      when(logsService.getLogsByDate(anyString(), any())).thenReturn(List.of());

      mockMvc
          .perform(get("/logs/2026-05-03"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.date").value("2026-05-03"))
          .andExpect(jsonPath("$.logs").isEmpty());
    }

    @Test
    @DisplayName("should return 400 when date format is invalid")
    void shouldReturn400WhenDateFormatIsInvalid() throws Exception {
      mockMvc.perform(get("/logs/invalid-date")).andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("GET /logs/request/{date}")
  class GetRequestLogsByDateTests {

    @Test
    @DisplayName("should return request logs when date is valid")
    void shouldReturnRequestLogsWhenDateIsValid() throws Exception {
      RequestLogsDTO requestLog = new RequestLogsDTO();
      requestLog.setMethod("GET");
      requestLog.setPath("/api/users");
      requestLog.setStatus(200);
      requestLog.setUsername("testuser");
      requestLog.setDuration(10L);

      when(logsService.getRequestLogs(any(RequestLogsFilterDTO.class), anyString()))
          .thenReturn(List.of(requestLog));

      mockMvc
          .perform(get("/logs/request/2026-05-03"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].method").value("GET"))
          .andExpect(jsonPath("$[0].path").value("/api/users"))
          .andExpect(jsonPath("$[0].status").value(200));
    }

    @Test
    @DisplayName("should return empty list when no request logs found")
    void shouldReturnEmptyListWhenNoRequestLogsFound() throws Exception {
      when(logsService.getRequestLogs(any(RequestLogsFilterDTO.class), anyString()))
          .thenReturn(List.of());

      mockMvc
          .perform(get("/logs/request/2026-05-03"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("should return 400 when date format is invalid")
    void shouldReturn400WhenDateFormatIsInvalid() throws Exception {
      mockMvc.perform(get("/logs/request/invalid-date")).andExpect(status().isBadRequest());
    }
  }
}
