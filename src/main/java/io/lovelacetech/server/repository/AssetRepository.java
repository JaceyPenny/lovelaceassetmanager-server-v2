package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface AssetRepository extends JpaRepository<Asset, UUID> {
  List<Asset> findAllByDeviceId(UUID deviceId);

  List<Asset> findAllByDeviceIdIn(List<UUID> deviceIds);

  List<Asset> findAllByLocationId(UUID locationId);

  List<Asset> findAllByLocationIdIn(List<UUID> locationIds);

  List<Asset> findAllByHomeId(UUID homeId);

  Asset findOneByRfid(String rfid);

  List<Asset> findAllByRfidIn(List<String> rfids);

  int countAllByHomeId(UUID homeId);

  int countAllByDeviceId(UUID deviceId);

  int countAllByLocationId(UUID locationId);

  List<Asset> findAllByAssetTypeEquals(AssetType assetType);
}
