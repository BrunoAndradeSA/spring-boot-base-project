package com.brunoandradesa.api.repository;

import com.brunoandradesa.api.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  @Query(
      """
      SELECT u
        FROM User u
       WHERE u.username = :username
         AND u.deletedAt IS NULL
      """)
  Optional<User> findActiveByUsername(String username);
}
