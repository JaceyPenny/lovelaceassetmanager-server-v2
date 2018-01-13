package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface LocationRepository extends CrudRepository<Location, UUID> {
  List<Location> findAllByCompanyId(UUID companyId);
}
