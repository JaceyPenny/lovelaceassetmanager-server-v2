package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.Asset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface AssetRepository extends CrudRepository<Asset, UUID> {
  List<Asset> findAllByDeviceId(UUID deviceId);

  List<Asset> findAllByDeviceIdIn(List<UUID> deviceIds);

  List<Asset> findAllByLocationId(UUID locationId);

  List<Asset> findAllByLocationIdIn(List<UUID> locationIds);
}
