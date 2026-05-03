package com.brunoandradesa.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final String JWT_SECRET;
  private final Long EXPIRATION;

  public JwtService(
      @Value("${api.secret-key}") String jwtSecret,
      @Value("${api.expiration-token}") String expirationToken) {
    this.JWT_SECRET = jwtSecret;
    this.EXPIRATION = 1000 * 60 * Long.valueOf(expirationToken);
  }

  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
  }

  public String generateToken(
      String username,
      Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> extraClaims) {

    Map<String, Object> claims = new HashMap<>(extraClaims);

    claims.put("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList());

    return Jwts.builder()
        .claims(claims)
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .signWith(getKey())
        .compact();
  }

  public String extractUsername(String token) {
    return getClaims(token).getSubject();
  }

  public Claims getClaims(String token) {
    return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
  }
}
