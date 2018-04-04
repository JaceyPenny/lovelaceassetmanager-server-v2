package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, UUID> {
  User findByEmail(String email);

  User findByEmailAndPassword(String email, String password);

  User findByUsername(String username);

  List<User> findByCompanyId(UUID companyId);

  List<User> findAllByIdIn(List<UUID> uuids);
}
