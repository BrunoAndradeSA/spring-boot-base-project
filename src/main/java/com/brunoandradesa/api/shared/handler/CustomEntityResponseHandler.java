package com.brunoandradesa.api.shared.handler;

import com.brunoandradesa.api.shared.dto.DefaultResponseDTO;
import com.brunoandradesa.api.shared.exception.BadRequestException;
import com.brunoandradesa.api.shared.exception.NotFoundException;
import com.brunoandradesa.api.shared.util.AppConstants;
import java.util.Date;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class CustomEntityResponseHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BadCredentialsException.class)
  public final ResponseEntity<DefaultResponseDTO> handleBadCredentialsException(
      BadCredentialsException ex, WebRequest request) {
    DefaultResponseDTO response =
        new DefaultResponseDTO(
            AppConstants.SC_GENERIC_ERROR,
            ex.getMessage(),
            request.getDescription(false),
            new Date());

    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(DisabledException.class)
  public final ResponseEntity<DefaultResponseDTO> handleDisabledException(
      DisabledException ex, WebRequest request) {
    DefaultResponseDTO response =
        new DefaultResponseDTO(
            AppConstants.SC_GENERIC_ERROR,
            ex.getMessage(),
            request.getDescription(false),
            new Date());

    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(LockedException.class)
  public final ResponseEntity<DefaultResponseDTO> handleLockedException(
      LockedException ex, WebRequest request) {
    DefaultResponseDTO response =
        new DefaultResponseDTO(
            AppConstants.SC_GENERIC_ERROR,
            ex.getMessage(),
            request.getDescription(false),
            new Date());

    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(NotFoundException.class)
  public final ResponseEntity<DefaultResponseDTO> handleNotFoundException(
      NotFoundException ex, WebRequest request) {
    DefaultResponseDTO response =
        new DefaultResponseDTO(
            AppConstants.SC_GENERIC_ERROR,
            ex.getMessage(),
            request.getDescription(false),
            new Date());

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadRequestException.class)
  public final ResponseEntity<DefaultResponseDTO> handleBadRequestException(
      BadRequestException ex, WebRequest request) {
    DefaultResponseDTO response =
        new DefaultResponseDTO(
            AppConstants.SC_GENERIC_ERROR,
            ex.getMessage(),
            request.getDescription(false),
            new Date());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleNoResourceFoundException(
      NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    DefaultResponseDTO response =
        new DefaultResponseDTO(
            AppConstants.SC_GENERIC_ERROR,
            ex.getMessage(),
            request.getDescription(false),
            new Date());

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    DefaultResponseDTO response =
        new DefaultResponseDTO(
            AppConstants.SC_GENERIC_ERROR,
            ex.getMessage(),
            request.getDescription(false),
            new Date());

    return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();

    DefaultResponseDTO response =
        new DefaultResponseDTO(
            AppConstants.SC_GENERIC_ERROR,
            String.join(", ", errors),
            request.getDescription(false),
            new Date());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
