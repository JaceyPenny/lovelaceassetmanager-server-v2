package io.lovelacetech.server.util;

import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.Log;
import io.lovelacetech.server.model.api.enums.LogType;
import io.lovelacetech.server.model.api.model.*;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.repository.LogRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class LogUtil {
  public static void removeAssetsFromDeviceAndLog(
      List<ApiAsset> assets,
      UUID fromDeviceId,
      UUID toLocationId,
      AssetRepository assetRepository,
      LogRepository logRepository) {
    List<Log> logs = new ArrayList<>();

    for (ApiAsset asset : assets) {
      asset.setDeviceId(null);
      asset.setLocationId(toLocationId);

      Log newLog = createRemoveAssetFromDeviceLog(asset, fromDeviceId, toLocationId);
      logs.add(newLog);
    }

    List<Asset> dbAssets = RepositoryUtils.toDatabaseList(assets);
    assetRepository.save(dbAssets);

    logRepository.save(logs);
  }

  public static void addAssetsToDeviceAndLog(
      List<ApiAsset> assets,
      UUID fromLocationId,
      UUID toDeviceId,
      AssetRepository assetRepository,
      LogRepository logRepository) {
    List<Log> logs = new ArrayList<>();

    for (ApiAsset asset : assets) {
      asset.setDeviceId(toDeviceId);
      asset.setLocationId(null);

      Log newLog = createAddAssetToDeviceLog(asset, fromLocationId, toDeviceId);
      logs.add(newLog);
    }

    List<Asset> dbAssets = RepositoryUtils.toDatabaseList(assets);
    assetRepository.save(dbAssets);

    logRepository.save(logs);
  }

  private static Log createRemoveAssetFromDeviceLog(ApiAsset asset, UUID fromDeviceId, UUID toLocationId) {
    Log newLog = new Log();

    newLog.setObjectId(asset.getId());
    newLog.setType(LogType.ASSET_MOVED);
    newLog.setTimestamp(LocalDateTime.now());
    newLog.setOldData(Collections.singletonMap("device_id", fromDeviceId));
    newLog.setNewData(Collections.singletonMap("location_id", toLocationId));

    return newLog;
  }

  private static Log createAddAssetToDeviceLog(ApiAsset asset, UUID fromLocationId, UUID toDeviceId) {
    Log newLog = new Log();

    newLog.setObjectId(asset.getId());
    newLog.setType(LogType.ASSET_MOVED);
    newLog.setTimestamp(LocalDateTime.now());
    newLog.setOldData(Collections.singletonMap("location_id", fromLocationId));
    newLog.setNewData(Collections.singletonMap("device_id", toDeviceId));

    return newLog;
  }

  public static void deleteDeviceAndLog(
      ApiUser user,
      ApiDevice device,
      DeviceRepository deviceRepository,
      LogRepository logRepository,
      boolean hardDelete) {
    Log deleteDeviceLog = createDeleteDeviceLog(device, user);

    if (hardDelete) {
      deviceRepository.delete(device.getId());
    } else {
      device.setLocationId(null);
      deviceRepository.save(device.toDatabase());
    }

    logRepository.save(deleteDeviceLog);
  }

  private static Log createDeleteDeviceLog(ApiDevice device, ApiUser user) {
    Log newLog = new Log();

    newLog.setObjectId(device.getId());
    newLog.setUserId(user.getId());
    newLog.setType(LogType.DEVICE_DELETED);
    newLog.setTimestamp(LocalDateTime.now());
    newLog.setOldData(device.toLogObject());

    return newLog;
  }

  public static void deleteLocationAndLog(
      ApiUser user,
      ApiLocation location,
      LocationRepository locationRepository,
      LogRepository logRepository) {
    locationRepository.delete(location.getId());

    Log deleteLocationLog = createDeleteLocationLog(location, user);
    logRepository.save(deleteLocationLog);
  }

  private static Log createDeleteLocationLog(ApiLocation location, ApiUser user) {
    Log newLog = new Log();

    newLog.setObjectId(location.getId());
    newLog.setUserId(user.getId());
    newLog.setType(LogType.LOCATION_DELETED);
    newLog.setTimestamp(LocalDateTime.now());
    newLog.setOldData(location.toLogObject());

    return newLog;
  }
}
