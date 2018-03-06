package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource
public interface InviteRepository extends JpaRepository<Invite, UUID> {

}
