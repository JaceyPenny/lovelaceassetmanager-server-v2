package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.PasswordReset;
import io.lovelacetech.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, UUID> {
  PasswordReset findByCode(String code);

  PasswordReset findByUser(User user);
}
