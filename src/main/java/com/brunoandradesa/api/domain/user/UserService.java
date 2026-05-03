package com.brunoandradesa.api.domain.user;

import com.brunoandradesa.api.domain.role.Role;
import com.brunoandradesa.api.dto.request.UserCreateDTO;
import com.brunoandradesa.api.dto.request.UserFilterDTO;
import com.brunoandradesa.api.dto.request.UserUpdateDTO;
import com.brunoandradesa.api.dto.response.UserDTO;
import com.brunoandradesa.api.mapper.UserMapper;
import com.brunoandradesa.api.repository.RolesRepository;
import com.brunoandradesa.api.repository.UserRepository;
import com.brunoandradesa.api.shared.exception.BadRequestException;
import com.brunoandradesa.api.shared.exception.NotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RolesRepository rolesRepository;

  public List<UserDTO> get(UserFilterDTO filter) {
    Specification<User> spec =
        Specification.where(UserSpecification.usernameContains(filter.getUsername()))
            .and(UserSpecification.hasEnabled(filter.getEnabled()))
            .and(UserSpecification.hasRoles(filter.getRoles()));

    return userRepository.findAll(spec).stream().map(UserMapper::toDTO).toList();
  }

  public UserDTO getById(Long id) {
    User user = getUserById(id);

    return UserMapper.toDTO(user);
  }

  public UserDTO create(UserCreateDTO dto) {
    User user = new User();

    user.setUsername(dto.getUsername());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));

    user.setEnabled(true);
    user.setAccountNonLocked(true);
    user.setFailedAttempts(0);
    user.setCreatedAt(new Date());

    Set<Role> roles = rolesRepository.findByNameIn(dto.getRoles());

    if (roles.size() != dto.getRoles().size()) {
      throw new BadRequestException("Uma ou mais roles são inválidas para criação do usuário");
    }

    user.setRoles(roles);

    return UserMapper.toDTO(userRepository.save(user));
  }

  public UserDTO update(Long id, UserUpdateDTO dto) {
    User user = getUserById(id);

    if (dto.getPassword() != null) user.setPassword(passwordEncoder.encode(dto.getPassword()));

    if (dto.getRoles() != null) {
      Set<Role> roles = rolesRepository.findByNameIn(dto.getRoles());

      if (roles.size() != dto.getRoles().size()) {
        throw new BadRequestException("Uma ou mais roles são inválidas para criação do usuário");
      }

      user.setRoles(roles);
    }

    user.setUpdatedAt(new Date());

    return UserMapper.toDTO(userRepository.save(user));
  }

  public UserDTO changeUserStatus(Long id, Boolean enabled) {
    User user = getUserById(id);

    user.setEnabled(enabled);

    return UserMapper.toDTO(userRepository.save(user));
  }

  public void delete(Long id) {
    User user = getUserById(id);

    userRepository.delete(user);
  }

  private User getUserById(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(
                () -> new NotFoundException("Usuário não localizado pelo ID %s".formatted(id)));

    return user;
  }
}
