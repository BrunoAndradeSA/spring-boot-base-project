package com.brunoandradesa.api.repository;

import com.brunoandradesa.api.domain.role.Role;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {

  Set<Role> findByNameIn(Set<String> names);
}
