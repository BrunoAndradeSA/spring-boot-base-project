package com.brunoandradesa.api.shared.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.brunoandradesa.api.shared.dto.DefaultResponseDTO;
import com.brunoandradesa.api.shared.exception.BadRequestException;
import com.brunoandradesa.api.shared.exception.NotFoundException;
import com.brunoandradesa.api.shared.util.AppConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ExtendWith(MockitoExtension.class)
class CustomEntityResponseHandlerTest {

  private final CustomEntityResponseHandler handler = new CustomEntityResponseHandler();

  private WebRequest mockWebRequest;

  @Nested
  @DisplayName("handleBadCredentialsException")
  class BadCredentialsExceptionTests {

    @Test
    @DisplayName("should return 401 when BadCredentialsException is thrown")
    void shouldReturn401WhenBadCredentials() {
      mockWebRequest = mock(WebRequest.class);
      when(mockWebRequest.getDescription(false)).thenReturn("description");

      BadCredentialsException ex = new BadCredentialsException("Invalid credentials");
      ResponseEntity<DefaultResponseDTO> response =
          handler.handleBadCredentialsException(ex, mockWebRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
      assertThat(response.getBody())
          .satisfies(
              body -> {
                assertThat(body.getStatus()).isEqualTo(AppConstants.SC_GENERIC_ERROR);
                assertThat(body.getMessage()).isEqualTo("Invalid credentials");
              });
    }
  }

  @Nested
  @DisplayName("handleDisabledException")
  class DisabledExceptionTests {

    @Test
    @DisplayName("should return 403 when DisabledException is thrown")
    void shouldReturn403WhenDisabled() {
      mockWebRequest = mock(WebRequest.class);
      when(mockWebRequest.getDescription(false)).thenReturn("description");

      DisabledException ex = new DisabledException("User disabled");
      ResponseEntity<DefaultResponseDTO> response =
          handler.handleDisabledException(ex, mockWebRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
      assertThat(response.getBody())
          .satisfies(
              body -> {
                assertThat(body.getStatus()).isEqualTo(AppConstants.SC_GENERIC_ERROR);
                assertThat(body.getMessage()).isEqualTo("User disabled");
              });
    }
  }

  @Nested
  @DisplayName("handleLockedException")
  class LockedExceptionTests {

    @Test
    @DisplayName("should return 403 when LockedException is thrown")
    void shouldReturn403WhenLocked() {
      mockWebRequest = mock(WebRequest.class);
      when(mockWebRequest.getDescription(false)).thenReturn("description");

      LockedException ex = new LockedException("Account locked");
      ResponseEntity<DefaultResponseDTO> response =
          handler.handleLockedException(ex, mockWebRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
      assertThat(response.getBody())
          .satisfies(
              body -> {
                assertThat(body.getStatus()).isEqualTo(AppConstants.SC_GENERIC_ERROR);
                assertThat(body.getMessage()).isEqualTo("Account locked");
              });
    }
  }

  @Nested
  @DisplayName("handleNotFoundException")
  class NotFoundExceptionTests {

    @Test
    @DisplayName("should return 404 when NotFoundException is thrown")
    void shouldReturn404WhenNotFound() {
      mockWebRequest = mock(WebRequest.class);
      when(mockWebRequest.getDescription(false)).thenReturn("description");

      NotFoundException ex = new NotFoundException("Resource not found");
      ResponseEntity<DefaultResponseDTO> response =
          handler.handleNotFoundException(ex, mockWebRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      assertThat(response.getBody())
          .satisfies(
              body -> {
                assertThat(body.getStatus()).isEqualTo(AppConstants.SC_GENERIC_ERROR);
                assertThat(body.getMessage()).isEqualTo("Resource not found");
              });
    }
  }

  @Nested
  @DisplayName("handleBadRequestException")
  class BadRequestExceptionTests {

    @Test
    @DisplayName("should return 400 when BadRequestException is thrown")
    void shouldReturn400WhenBadRequest() {
      mockWebRequest = mock(WebRequest.class);
      when(mockWebRequest.getDescription(false)).thenReturn("description");

      BadRequestException ex = new BadRequestException("Bad request error");
      ResponseEntity<DefaultResponseDTO> response =
          handler.handleBadRequestException(ex, mockWebRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
      assertThat(response.getBody())
          .satisfies(
              body -> {
                assertThat(body.getStatus()).isEqualTo(AppConstants.SC_GENERIC_ERROR);
                assertThat(body.getMessage()).isEqualTo("Bad request error");
              });
    }
  }

  @Nested
  @DisplayName("handleNoResourceFoundException")
  class NoResourceFoundExceptionTests {

    @Test
    @DisplayName("should return 404 when NoResourceFoundException is thrown")
    void shouldReturn404WhenNoResourceFound() {
      mockWebRequest = mock(WebRequest.class);
      when(mockWebRequest.getDescription(false)).thenReturn("description");

      NoResourceFoundException ex =
          new NoResourceFoundException(
              org.springframework.http.HttpMethod.GET, "/test", "No resource found");
      ResponseEntity<Object> response =
          handler.handleNoResourceFoundException(
              ex, new org.springframework.http.HttpHeaders(), HttpStatus.NOT_FOUND, mockWebRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      assertThat(response.getBody())
          .satisfies(
              body -> {
                DefaultResponseDTO dto = (DefaultResponseDTO) body;
                assertThat(dto.getStatus()).isEqualTo(AppConstants.SC_GENERIC_ERROR);
              });
    }
  }

  @Nested
  @DisplayName("handleHttpRequestMethodNotSupported")
  class HttpRequestMethodNotSupportedExceptionTests {

    @Test
    @DisplayName("should return 405 when HttpRequestMethodNotSupportedException is thrown")
    void shouldReturn405WhenMethodNotSupported() {
      mockWebRequest = mock(WebRequest.class);
      when(mockWebRequest.getDescription(false)).thenReturn("description");

      HttpRequestMethodNotSupportedException ex =
          new HttpRequestMethodNotSupportedException("POST");
      ResponseEntity<Object> response =
          handler.handleHttpRequestMethodNotSupported(
              ex,
              new org.springframework.http.HttpHeaders(),
              HttpStatus.METHOD_NOT_ALLOWED,
              mockWebRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
      assertThat(response.getBody())
          .satisfies(
              body -> {
                DefaultResponseDTO dto = (DefaultResponseDTO) body;
                assertThat(dto.getStatus()).isEqualTo(AppConstants.SC_GENERIC_ERROR);
              });
    }
  }

  @Nested
  @DisplayName("handleMethodArgumentNotValid")
  class MethodArgumentNotValidExceptionTests {

    @Test
    @DisplayName("should return 400 when validation fails with field errors")
    void shouldReturn400WhenValidationFails() {
      mockWebRequest = mock(WebRequest.class);
      when(mockWebRequest.getDescription(false)).thenReturn("description");

      MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
      BindingResult bindingResult = mock(BindingResult.class);
      FieldError fieldError = new FieldError("object", "field", "must not be blank");
      when(ex.getBindingResult()).thenReturn(bindingResult);
      when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(fieldError));

      ResponseEntity<Object> response =
          handler.handleMethodArgumentNotValid(
              ex,
              new org.springframework.http.HttpHeaders(),
              HttpStatus.BAD_REQUEST,
              mockWebRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
      assertThat(response.getBody())
          .satisfies(
              body -> {
                DefaultResponseDTO dto = (DefaultResponseDTO) body;
                assertThat(dto.getStatus()).isEqualTo(AppConstants.SC_GENERIC_ERROR);
                assertThat(dto.getMessage()).contains("field: must not be blank");
              });
    }
  }
}
