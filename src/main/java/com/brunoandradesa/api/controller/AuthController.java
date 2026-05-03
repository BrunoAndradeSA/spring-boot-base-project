package com.brunoandradesa.api.controller;

import com.brunoandradesa.api.controller.docs.AuthControllerDocs;
import com.brunoandradesa.api.domain.auth.AuthService;
import com.brunoandradesa.api.dto.request.LoginRequestDTO;
import com.brunoandradesa.api.dto.response.TokenDTO;
import jakarta.validation.Valid;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

  private final AuthService authService;

  @Override
  @PostMapping("/login")
  public TokenDTO login(@RequestBody @Valid LoginRequestDTO request) {
    String token = authService.login(request.getUsername(), request.getPassword());

    return new TokenDTO(token);
  }

  @Override
  @PostMapping("/client")
  public TokenDTO client(@RequestHeader("Authorization") String header) {
    String base64 = header.replace("Basic ", "");
    String decoded = new String(Base64.getDecoder().decode(base64));

    String[] values = decoded.split(":");

    String token = authService.clientLogin(values[0], values[1]);

    return new TokenDTO(token);
  }
}
