package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.sql.Time;
import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
  List<Notification> findAllByUserId(UUID userId);

  List<Notification> findByTimeEquals(Time time);
}
