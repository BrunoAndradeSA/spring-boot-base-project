package com.brunoandradesa.api.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

  public BadRequestException(String message) {
    super(message);
    log.error(message);
  }

  public BadRequestException(String message, Exception exception) {
    super(message, exception);
    log.error(message);
  }
}
