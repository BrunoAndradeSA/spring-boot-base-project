package com.brunoandradesa.api.domain.logs;

import com.brunoandradesa.api.dto.request.RequestLogsFilterDTO;
import com.brunoandradesa.api.dto.response.RequestLogsDTO;
import com.brunoandradesa.api.mapper.RequestLogsMapper;
import com.brunoandradesa.api.repository.RequestLogsRepository;
import com.brunoandradesa.api.shared.exception.BadRequestException;
import com.brunoandradesa.api.shared.exception.NotFoundException;
import com.brunoandradesa.api.shared.util.AppConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogsService {

  private static final String LOG_PATH = "logs";
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final RequestLogsRepository requestLogsRepository;

  public List<Map<String, Object>> getLogsByDate(String date, String level) {

    String fileName = LOG_PATH + "/" + date + "-logs.log";
    Path path = Paths.get(fileName);

    if (!Files.exists(path)) {
      throw new NotFoundException("Log file not found for date: " + date);
    }

    List<Map<String, Object>> logs = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(path)) {

      String line;

      while ((line = reader.readLine()) != null) {

        if (line.isBlank()) continue;

        Map<String, Object> json =
            objectMapper.readValue(line, new TypeReference<Map<String, Object>>() {});

        if (level != null && !level.isBlank()) {
          String logLevel = (String) json.get("level");

          if (logLevel == null || !logLevel.equalsIgnoreCase(level)) {
            continue;
          }
        }

        logs.add(json);
      }

    } catch (IOException e) {
      throw new BadRequestException("Error reading log file", e);
    }

    return logs;
  }

  public List<RequestLogsDTO> getRequestLogs(RequestLogsFilterDTO filter, String date) {
    Specification<RequestLogs> spec =
        Specification.where(RequestLogsSpecification.usernameContains(filter.getUsername()))
            .and(RequestLogsSpecification.pathContains(filter.getPath()))
            .and(RequestLogsSpecification.dateEquals(date))
            .and(RequestLogsSpecification.equalsTo("method", filter.getMethod()))
            .and(RequestLogsSpecification.equalsTo("status", filter.getStatus()));

    return requestLogsRepository.findAll(spec).stream().map(RequestLogsMapper::toDTO).toList();
  }

  @Async
  public void saveRequestLog(
      HttpServletRequest request,
      HttpServletResponse response,
      String requestBody,
      String responseBody,
      long duration) {

    String method = request.getMethod();
    String path = request.getRequestURI();
    int status = response.getStatus();

    String headers = extractHeaders(request);

    String curl = buildCurl(request, requestBody);

    String user = getAuthenticatedUser();

    RequestLogs requestLogs = new RequestLogs();

    requestLogs.setMethod(method);
    requestLogs.setPath(path);
    requestLogs.setStatus(status);
    requestLogs.setHeaders(headers);
    requestLogs.setCurl(curl);
    requestLogs.setUser(user);
    requestLogs.setRequestBody(requestBody);
    requestLogs.setResponseBody(responseBody);
    requestLogs.setDuration(duration);
    requestLogs.setCreatedAt(LocalDateTime.now());

    requestLogsRepository.save(requestLogs);
  }

  private String buildCurl(HttpServletRequest request, String body) {
    StringBuilder curl =
        new StringBuilder("curl -X ")
            .append(request.getMethod())
            .append(" '")
            .append(request.getRequestURL())
            .append("' ");

    Collections.list(request.getHeaderNames())
        .forEach(
            h ->
                curl.append("-H '")
                    .append(h)
                    .append(": ")
                    .append(request.getHeader(h))
                    .append("' "));

    if (body != null && !body.isBlank()) {
      curl.append("-d '").append(body).append("' ");
    }

    return curl.toString();
  }

  private String getAuthenticatedUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth != null ? auth.getName() : "anonymous";
  }

  private String extractHeaders(HttpServletRequest request) {
    return Collections.list(request.getHeaderNames()).stream()
        .map(headerName -> formatHeader(headerName, request))
        .collect(Collectors.joining("\n"));
  }

  private String formatHeader(String headerName, HttpServletRequest request) {

    String value = request.getHeader(headerName);

    if (isSensitive(headerName)) {
      value = mask(value);
    }

    return headerName + ": " + value;
  }

  private boolean isSensitive(String headerName) {
    return AppConstants.SENSITIVE_HEADERS.contains(headerName.toLowerCase());
  }

  private String mask(String value) {
    if (value == null || value.isBlank()) {
      return value;
    }

    return value.length() <= 10 ? "*****" : value.substring(0, 10) + "...";
  }
}
