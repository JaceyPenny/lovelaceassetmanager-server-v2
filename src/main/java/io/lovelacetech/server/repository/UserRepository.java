package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, UUID> {
  User findByEmail(String email);

  User findByEmailAndPassword(String email, String password);

  User findByUsername(String username);
}
