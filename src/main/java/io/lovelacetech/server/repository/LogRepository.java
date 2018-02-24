package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.Log;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface LogRepository extends CrudRepository<Log, UUID> {
  List<Log> findAllByObjectId(UUID objectId);

  Log findFirstByObjectIdOrderByTimestampDesc(UUID objectId);
}
