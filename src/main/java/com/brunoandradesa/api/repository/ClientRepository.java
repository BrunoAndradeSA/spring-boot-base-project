package com.brunoandradesa.api.repository;

import com.brunoandradesa.api.domain.client.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

  @Query(
      """
      SELECT c
        FROM Client c
       WHERE c.clientId = :clientId
         AND c.deletedAt IS NULL
      """)
  Optional<Client> findActiveByClientId(String clientId);
}
