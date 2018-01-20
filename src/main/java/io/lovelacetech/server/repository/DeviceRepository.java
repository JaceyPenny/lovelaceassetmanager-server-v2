package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface DeviceRepository extends CrudRepository<Device, UUID> {
  Device findOneByDeviceCode(String deviceCode);

  List<Device> findAllByLocationId(UUID locationId);

  List<Device> findAllByLocationIdIn(List<UUID> locationIds);
}
