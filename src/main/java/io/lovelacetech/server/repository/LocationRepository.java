package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface LocationRepository extends JpaRepository<Location, UUID> {
  List<Location> findAllByIdIn(List<UUID> locationIds);

  List<Location> findAllByCompanyId(UUID companyId);

  Location findByCompanyIdAndName(UUID companyId, String name);
}
