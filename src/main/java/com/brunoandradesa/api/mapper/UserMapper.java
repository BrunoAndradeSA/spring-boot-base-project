package com.brunoandradesa.api.mapper;

import com.brunoandradesa.api.domain.user.User;
import com.brunoandradesa.api.dto.response.UserDTO;
import java.util.stream.Collectors;

public class UserMapper {

  public static UserDTO toDTO(User user) {
    return new UserDTO(
        user.getId(),
        user.getUsername(),
        user.isEnabled(),
        user.isAccountNonLocked(),
        user.getFailedAttempts(),
        user.getLastLogin(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()));
  }
}
