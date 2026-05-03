package com.brunoandradesa.api.domain.auth;

import com.brunoandradesa.api.domain.user.User;
import com.brunoandradesa.api.repository.UserRepository;
import com.brunoandradesa.api.security.JwtService;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public String login(String username, String password) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

    setUserLastLogin((User) authentication.getPrincipal());

    return jwtService.generateToken(username, authorities, Map.of("type", "USER"));
  }

  public String clientLogin(String clientId, String secret) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(clientId, secret));

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

    return jwtService.generateToken(clientId, authorities, Map.of("type", "CLIENT"));
  }

  private void setUserLastLogin(User user) {
    user.setLastLogin(new Date());

    userRepository.save(user);
  }
}
