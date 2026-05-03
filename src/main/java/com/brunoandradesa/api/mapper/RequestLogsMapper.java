package com.brunoandradesa.api.mapper;

import com.brunoandradesa.api.domain.logs.RequestLogs;
import com.brunoandradesa.api.dto.response.RequestLogsDTO;

public class RequestLogsMapper {

  public static RequestLogsDTO toDTO(RequestLogs requestLogs) {
    return new RequestLogsDTO(
        requestLogs.getCreatedAt(),
        requestLogs.getMethod(),
        requestLogs.getPath(),
        requestLogs.getStatus(),
        requestLogs.getUser(),
        requestLogs.getDuration(),
        requestLogs.getHeaders(),
        requestLogs.getRequestBody(),
        requestLogs.getResponseBody(),
        requestLogs.getCurl());
  }
}
