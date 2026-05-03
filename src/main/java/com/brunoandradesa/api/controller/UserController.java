package com.brunoandradesa.api.controller;

import com.brunoandradesa.api.controller.docs.UserControllerDocs;
import com.brunoandradesa.api.domain.user.UserService;
import com.brunoandradesa.api.dto.request.UserCreateDTO;
import com.brunoandradesa.api.dto.request.UserFilterDTO;
import com.brunoandradesa.api.dto.request.UserUpdateDTO;
import com.brunoandradesa.api.dto.response.UserDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

  private final UserService userService;

  @Override
  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN','SYSTEM')")
  public List<UserDTO> getUsers(@ParameterObject UserFilterDTO filter) {
    return userService.get(filter);
  }

  @Override
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','SYSTEM')")
  public UserDTO getUserById(@PathVariable Long id) {
    return userService.getById(id);
  }

  @Override
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','SYSTEM')")
  public UserDTO createUser(@RequestBody @Valid UserCreateDTO request) {
    return userService.create(request);
  }

  @Override
  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','SYSTEM')")
  public UserDTO updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO request) {
    return userService.update(id, request);
  }

  @Override
  @PutMapping("/{id}/enable")
  public UserDTO enableUser(@PathVariable Long id) {
    return userService.changeUserStatus(id, true);
  }

  @Override
  @PutMapping("/{id}/disable")
  public UserDTO disableUser(@PathVariable Long id) {
    return userService.changeUserStatus(id, false);
  }

  @Override
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','SYSTEM')")
  public void deleteUser(@PathVariable Long id) {
    userService.delete(id);
  }
}
