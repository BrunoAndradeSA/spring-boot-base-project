package com.brunoandradesa.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final AuthenticationEntryPoint authenticationEntryPoint;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String header = request.getHeader("Authorization");

      if (header != null && header.startsWith("Bearer ")) {
        String token = header.substring(7);

        Claims claims = jwtService.getClaims(token);

        String username = claims.getSubject();

        Object rolesObj = claims.get("roles");

        List<String> roles;

        if (rolesObj instanceof List<?> list) {
          roles = list.stream().filter(String.class::isInstance).map(String.class::cast).toList();
        } else {
          roles = List.of();
        }

        List<GrantedAuthority> authorities =
            roles.stream().<GrantedAuthority>map(SimpleGrantedAuthority::new).toList();

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

          UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(username, null, authorities);

          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }

      filterChain.doFilter(request, response);
    } catch (ExpiredJwtException ex) {
      authenticationEntryPoint.commence(
          request, response, new InsufficientAuthenticationException("Token expirado"));
    } catch (UnsupportedJwtException ex) {
      authenticationEntryPoint.commence(
          request, response, new InsufficientAuthenticationException("Token não suportado"));
    } catch (MalformedJwtException ex) {
      authenticationEntryPoint.commence(
          request, response, new InsufficientAuthenticationException("Token inválido"));
    } catch (SignatureException ex) {
      authenticationEntryPoint.commence(
          request, response, new InsufficientAuthenticationException("Assinatura inválida"));
    } catch (IllegalArgumentException ex) {
      authenticationEntryPoint.commence(
          request, response, new InsufficientAuthenticationException("Token vazio ou inválido"));
    }
  }
}
