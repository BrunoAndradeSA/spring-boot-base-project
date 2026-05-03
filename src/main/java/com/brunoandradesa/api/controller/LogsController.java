package com.brunoandradesa.api.controller;

import com.brunoandradesa.api.controller.docs.LogsControllerDocs;
import com.brunoandradesa.api.domain.logs.LogsService;
import com.brunoandradesa.api.dto.request.RequestLogsFilterDTO;
import com.brunoandradesa.api.dto.response.LogsDTO;
import com.brunoandradesa.api.dto.response.RequestLogsDTO;
import com.brunoandradesa.api.shared.util.AppConstants;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogsController implements LogsControllerDocs {

  private final LogsService logsService;

  @Override
  @GetMapping("/{date}")
  @PreAuthorize("hasAnyRole('ADMIN','SYSTEM')")
  public ResponseEntity<LogsDTO> getLogsByDate(
      @PathVariable @Pattern(regexp = AppConstants.REGEXP_DATE) String date,
      @RequestParam(required = false) String level) {

    List<Map<String, Object>> logs = logsService.getLogsByDate(date, level);

    LogsDTO response = new LogsDTO(date, logs);

    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/request/{date}")
  @PreAuthorize("hasAnyRole('ADMIN','SYSTEM')")
  public List<RequestLogsDTO> getRequestLogsByDate(
      @PathVariable @Pattern(regexp = AppConstants.REGEXP_DATE) String date,
      @ParameterObject RequestLogsFilterDTO filter) {
    return logsService.getRequestLogs(filter, date);
  }
}
