package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface NotificationRepository extends CrudRepository<Notification, UUID> {
  List<Notification> findAllByUserId(UUID userId);
}
