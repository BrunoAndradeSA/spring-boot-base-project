package com.brunoandradesa.api.domain.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brunoandradesa.api.domain.user.User;
import com.brunoandradesa.api.repository.UserRepository;
import com.brunoandradesa.api.security.JwtService;
import java.util.Collection;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private AuthenticationManager authenticationManager;
  @Mock private JwtService jwtService;

  @InjectMocks private AuthService authService;

  private User user;
  private Collection<? extends GrantedAuthority> authorities;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setUsername("testuser");
    authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Nested
  @DisplayName("login")
  class LoginTests {

    @Test
    @DisplayName("should return token when credentials are valid")
    void shouldReturnTokenWhenCredentialsAreValid() {
      Authentication authentication = mock(Authentication.class);
      when(authentication.getPrincipal()).thenReturn(user);
      when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
      when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
          .thenReturn(authentication);
      when(jwtService.generateToken(any(), any(), any())).thenReturn("jwt-token");
      when(userRepository.save(any(User.class))).thenReturn(user);

      String token = authService.login("testuser", "password");

      assertEquals("jwt-token", token);
      verify(authenticationManager)
          .authenticate(new UsernamePasswordAuthenticationToken("testuser", "password"));
      verify(jwtService).generateToken("testuser", authorities, Map.of("type", "USER"));
      verify(userRepository).save(user);
    }

    @Test
    @DisplayName("should throw exception when credentials are invalid")
    void shouldThrowExceptionWhenCredentialsAreInvalid() {
      when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
          .thenThrow(
              new org.springframework.security.core.AuthenticationException(
                  "Invalid credentials") {});

      assertThrows(
          org.springframework.security.core.AuthenticationException.class,
          () -> authService.login("testuser", "wrongpassword"));
    }
  }

  @Nested
  @DisplayName("clientLogin")
  class ClientLoginTests {

    @Test
    @DisplayName("should return token when client credentials are valid")
    void shouldReturnTokenWhenClientCredentialsAreValid() {
      Authentication authentication = mock(Authentication.class);
      when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
      when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
          .thenReturn(authentication);
      when(jwtService.generateToken(any(), any(), any())).thenReturn("client-jwt-token");

      String token = authService.clientLogin("client-id", "client-secret");

      assertEquals("client-jwt-token", token);
      verify(authenticationManager)
          .authenticate(new UsernamePasswordAuthenticationToken("client-id", "client-secret"));
      verify(jwtService).generateToken("client-id", authorities, Map.of("type", "CLIENT"));
    }

    @Test
    @DisplayName("should throw exception when client credentials are invalid")
    void shouldThrowExceptionWhenClientCredentialsAreInvalid() {
      when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
          .thenThrow(
              new org.springframework.security.core.AuthenticationException(
                  "Invalid client credentials") {});

      assertThrows(
          org.springframework.security.core.AuthenticationException.class,
          () -> authService.clientLogin("client-id", "wrong-secret"));
    }
  }
}
