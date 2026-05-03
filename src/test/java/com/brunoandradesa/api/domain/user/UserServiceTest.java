package com.brunoandradesa.api.domain.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.brunoandradesa.api.domain.role.Role;
import com.brunoandradesa.api.dto.request.UserCreateDTO;
import com.brunoandradesa.api.dto.request.UserFilterDTO;
import com.brunoandradesa.api.dto.request.UserUpdateDTO;
import com.brunoandradesa.api.dto.response.UserDTO;
import com.brunoandradesa.api.repository.RolesRepository;
import com.brunoandradesa.api.repository.UserRepository;
import com.brunoandradesa.api.shared.exception.NotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private PasswordEncoder passwordEncoder;
  @Mock private UserRepository userRepository;
  @Mock private RolesRepository rolesRepository;

  @InjectMocks private UserService userService;

  private User user;
  private Role roleUser;
  private Role roleAdmin;
  private UserDTO userDTO;

  @BeforeEach
  void setUp() {
    roleUser = new Role(1L, "ROLE_USER");
    roleAdmin = new Role(2L, "ROLE_ADMIN");

    user = new User();
    user.setId(1L);
    user.setUsername("bruno.andrade");
    user.setPassword("encodedPassword");
    user.setEnabled(true);
    user.setAccountNonLocked(true);
    user.setFailedAttempts(0);
    user.setCreatedAt(new Date());
    user.setRoles(Set.of(roleUser, roleAdmin));

    userDTO = new UserDTO();
    userDTO.setId(1L);
    userDTO.setUsername("bruno.andrade");
    userDTO.setEnabled(true);
    userDTO.setAccountNonLocked(true);
    userDTO.setFailedAttempts(0);
    userDTO.setCreatedAt(user.getCreatedAt());
    userDTO.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));
  }

  @Nested
  @DisplayName("get - Search users with filters")
  class GetTests {

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("should return empty list when no users match filter")
    void shouldReturnEmptyListWhenNoUsersMatch() {
      UserFilterDTO filter = new UserFilterDTO();
      filter.setUsername("nonexistent");

      when(userRepository.findAll(any(Specification.class))).thenReturn(List.of());

      List<UserDTO> result = userService.get(filter);

      assertEquals(0, result.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("should return list of users when filter matches")
    void shouldReturnListOfUsersWhenFilterMatches() {
      UserFilterDTO filter = new UserFilterDTO();
      filter.setUsername("bruno");
      filter.setEnabled(true);
      filter.setRoles(List.of("ROLE_USER"));

      when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(user));

      List<UserDTO> result = userService.get(filter);

      assertEquals(1, result.size());
      assertEquals("bruno.andrade", result.get(0).getUsername());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("should return all users when filter is null")
    void shouldReturnAllUsersWhenFilterIsNull() {
      UserFilterDTO filter = new UserFilterDTO();

      when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(user));

      List<UserDTO> result = userService.get(filter);

      assertEquals(1, result.size());
    }
  }

  @Nested
  @DisplayName("getById - Get user by ID")
  class GetByIdTests {

    @Test
    @DisplayName("should return user when user exists")
    void shouldReturnUserWhenExists() {
      when(userRepository.findById(1L)).thenReturn(Optional.of(user));

      UserDTO result = userService.getById(1L);

      assertEquals(1L, result.getId());
      assertEquals("bruno.andrade", result.getUsername());
    }

    @Test
    @DisplayName("should throw NotFoundException when user does not exist")
    void shouldThrowExceptionWhenUserNotFound() {
      when(userRepository.findById(999L)).thenReturn(Optional.empty());

      NotFoundException exception =
          assertThrows(NotFoundException.class, () -> userService.getById(999L));

      assertEquals("Usuário não localizado pelo ID 999", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("create - Create new user")
  class CreateTests {

    @Test
    @DisplayName("should create user with valid roles")
    void shouldCreateUserWhenRolesAreValid() {
      UserCreateDTO dto = new UserCreateDTO();
      dto.setUsername("new.user");
      dto.setPassword("Password123");
      dto.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));

      when(passwordEncoder.encode("Password123")).thenReturn("encodedPassword");
      when(rolesRepository.findByNameIn(Set.of("ROLE_USER", "ROLE_ADMIN")))
          .thenReturn(Set.of(roleUser, roleAdmin));
      when(userRepository.save(any(User.class))).thenReturn(user);

      UserDTO result = userService.create(dto);

      assertEquals("bruno.andrade", result.getUsername());
      verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("should throw exception when roles are invalid")
    void shouldThrowExceptionWhenRolesAreInvalid() {
      UserCreateDTO dto = new UserCreateDTO();
      dto.setUsername("new.user");
      dto.setPassword("Password123");
      dto.setRoles(Set.of("ROLE_USER", "INVALID_ROLE"));

      when(rolesRepository.findByNameIn(Set.of("ROLE_USER", "INVALID_ROLE")))
          .thenReturn(Set.of(roleUser));

      var exception =
          assertThrows(
              com.brunoandradesa.api.shared.exception.BadRequestException.class,
              () -> userService.create(dto));

      assertEquals(
          "Uma ou mais roles são inválidas para criação do usuário", exception.getMessage());
    }

    @Test
    @DisplayName("should create user with default roles when roles are empty")
    void shouldCreateUserWithDefaultRolesWhenRolesAreEmpty() {
      UserCreateDTO dto = new UserCreateDTO();
      dto.setUsername("new.user");
      dto.setPassword("Password123");
      dto.setRoles(Set.of());

      when(passwordEncoder.encode("Password123")).thenReturn("encodedPassword");
      when(rolesRepository.findByNameIn(Set.of())).thenReturn(Set.of());
      when(userRepository.save(any(User.class))).thenReturn(user);

      UserDTO result = userService.create(dto);

      assertEquals("bruno.andrade", result.getUsername());
    }
  }

  @Nested
  @DisplayName("update - Update existing user")
  class UpdateTests {

    @Test
    @DisplayName("should update user password when provided")
    void shouldUpdateUserPasswordWhenProvided() {
      UserUpdateDTO dto = new UserUpdateDTO();
      dto.setPassword("NewPassword123");

      when(userRepository.findById(1L)).thenReturn(Optional.of(user));
      when(passwordEncoder.encode("NewPassword123")).thenReturn("newEncodedPassword");
      when(userRepository.save(any(User.class))).thenReturn(user);

      UserDTO result = userService.update(1L, dto);

      assertEquals("bruno.andrade", result.getUsername());
    }

    @Test
    @DisplayName("should update user roles when provided")
    void shouldUpdateUserRolesWhenProvided() {
      UserUpdateDTO dto = new UserUpdateDTO();
      dto.setRoles(Set.of("ROLE_ADMIN"));

      when(userRepository.findById(1L)).thenReturn(Optional.of(user));
      when(rolesRepository.findByNameIn(Set.of("ROLE_ADMIN"))).thenReturn(Set.of(roleAdmin));
      when(userRepository.save(any(User.class))).thenReturn(user);

      UserDTO result = userService.update(1L, dto);

      assertEquals("bruno.andrade", result.getUsername());
    }

    @Test
    @DisplayName("should throw exception when update roles are invalid")
    void shouldThrowExceptionWhenUpdateRolesAreInvalid() {
      UserUpdateDTO dto = new UserUpdateDTO();
      dto.setRoles(Set.of("INVALID_ROLE"));

      when(userRepository.findById(1L)).thenReturn(Optional.of(user));
      when(rolesRepository.findByNameIn(Set.of("INVALID_ROLE"))).thenReturn(Set.of());

      var exception =
          assertThrows(
              com.brunoandradesa.api.shared.exception.BadRequestException.class,
              () -> userService.update(1L, dto));

      assertEquals(
          "Uma ou mais roles são inválidas para criação do usuário", exception.getMessage());
    }

    @Test
    @DisplayName("should update user without changes when dto is empty")
    void shouldUpdateUserWithoutChangesWhenDtoIsEmpty() {
      UserUpdateDTO dto = new UserUpdateDTO();

      when(userRepository.findById(1L)).thenReturn(Optional.of(user));
      when(userRepository.save(any(User.class))).thenReturn(user);

      UserDTO result = userService.update(1L, dto);

      assertEquals("bruno.andrade", result.getUsername());
    }

    @Test
    @DisplayName("should throw NotFoundException when user to update does not exist")
    void shouldThrowExceptionWhenUserToUpdateNotFound() {
      UserUpdateDTO dto = new UserUpdateDTO();

      when(userRepository.findById(999L)).thenReturn(Optional.empty());

      NotFoundException exception =
          assertThrows(NotFoundException.class, () -> userService.update(999L, dto));

      assertEquals("Usuário não localizado pelo ID 999", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("changeUserStatus - Enable or disable user")
  class ChangeUserStatusTests {

    @Test
    @DisplayName("should enable user when status is true")
    void shouldEnableUserWhenStatusIsTrue() {
      when(userRepository.findById(1L)).thenReturn(Optional.of(user));
      when(userRepository.save(any(User.class))).thenReturn(user);

      UserDTO result = userService.changeUserStatus(1L, true);

      assertEquals(true, result.isEnabled());
    }

    @Test
    @DisplayName("should disable user when status is false")
    void shouldDisableUserWhenStatusIsFalse() {
      when(userRepository.findById(1L)).thenReturn(Optional.of(user));
      when(userRepository.save(any(User.class))).thenReturn(user);

      UserDTO result = userService.changeUserStatus(1L, false);

      assertEquals(false, result.isEnabled());
    }

    @Test
    @DisplayName("should throw NotFoundException when user to change status does not exist")
    void shouldThrowExceptionWhenUserNotFoundForStatusChange() {
      when(userRepository.findById(999L)).thenReturn(Optional.empty());

      NotFoundException exception =
          assertThrows(NotFoundException.class, () -> userService.changeUserStatus(999L, true));

      assertEquals("Usuário não localizado pelo ID 999", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("delete - Delete user")
  class DeleteTests {

    @Test
    @DisplayName("should delete user when user exists")
    void shouldDeleteUserWhenExists() {
      when(userRepository.findById(1L)).thenReturn(Optional.of(user));

      userService.delete(1L);

      verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("should throw NotFoundException when user to delete does not exist")
    void shouldThrowExceptionWhenUserNotFoundForDelete() {
      when(userRepository.findById(999L)).thenReturn(Optional.empty());

      NotFoundException exception =
          assertThrows(NotFoundException.class, () -> userService.delete(999L));

      assertEquals("Usuário não localizado pelo ID 999", exception.getMessage());
    }
  }
}
