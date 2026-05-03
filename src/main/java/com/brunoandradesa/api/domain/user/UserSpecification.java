package com.brunoandradesa.api.domain.user;

import jakarta.persistence.criteria.Join;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

  public static Specification<User> usernameContains(String username) {
    return (root, query, cb) ->
        username == null ? null : cb.like(root.get("username"), "%" + username + "%");
  }

  public static Specification<User> hasEnabled(Boolean enabled) {
    return (root, query, cb) -> enabled == null ? null : cb.equal(root.get("enabled"), enabled);
  }

  public static Specification<User> hasRoles(List<String> roles) {
    return (root, query, cb) -> {
      if (roles == null || roles.isEmpty()) return null;

      Join<Object, Object> roleJoin = root.join("roles");
      return roleJoin.get("name").in(roles);
    };
  }
}
