package io.lovelacetech.server.util;

import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.Log;
import io.lovelacetech.server.model.api.enums.LogType;
import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.model.ApiUser;
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
      device.setName("Device " + device.getDeviceCode());
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

  public static void registerAssetAndLog(
      ApiAsset asset,
      AssetRepository assetRepository,
      LogRepository logRepository) {
    assetRepository.save(asset.toDatabase());

    Log registerAssetLog = createRegisterAssetLog(asset);
    logRepository.save(registerAssetLog);
  }

  private static Log createRegisterAssetLog(ApiAsset asset) {
    Log newLog = new Log();

    newLog.setObjectId(asset.getId());
    newLog.setType(LogType.ASSET_REGISTERED);
    newLog.setTimestamp(LocalDateTime.now());
    newLog.setNewData(asset.toLogObject());

    return newLog;
  }

  public static void editAssetAndLog(
      ApiUser user,
      ApiAsset oldAsset,
      ApiAsset newAsset,
      AssetRepository assetRepository,
      LogRepository logRepository) {
    assetRepository.save(newAsset.toDatabase());

    Log editAssetLog = createEditAssetLog(user, oldAsset, newAsset);
    logRepository.save(editAssetLog);
  }

  private static Log createEditAssetLog(ApiUser user, ApiAsset oldAsset, ApiAsset newAsset) {
    Log newLog = new Log();

    newLog.setObjectId(newAsset.getId());
    newLog.setType(LogType.ASSET_EDITED);
    newLog.setUserId(user.getId());
    newLog.setTimestamp(LocalDateTime.now());
    newLog.setOldData(oldAsset.toLogObject());
    newLog.setNewData(newAsset.toLogObject());

    return newLog;
  }

  public static void deleteAssetAndLog(ApiUser user, ApiAsset asset, AssetRepository assetRepository, LogRepository logRepository) {
    assetRepository.delete(asset.getId());

    Log deleteAssetLog = createDeleteAssetLog(user, asset);
    logRepository.save(deleteAssetLog);
  }

  private static Log createDeleteAssetLog(ApiUser user, ApiAsset asset) {
    Log newLog = new Log();

    newLog.setObjectId(asset.getId());
    newLog.setType(LogType.ASSET_DELETED);
    newLog.setUserId(user.getId());
    newLog.setTimestamp(LocalDateTime.now());
    newLog.setOldData(asset.toLogObject());

    return newLog;
  }

  public static void registerDeviceAndLog(
      ApiUser user,
      ApiDevice device,
      DeviceRepository deviceRepository,
      LogRepository logRepository) {
    deviceRepository.save(device.toDatabase());

    Log registerDeviceLog = createRegisterDeviceLog(user, device);
    logRepository.save(registerDeviceLog);
  }

  private static Log createRegisterDeviceLog(ApiUser user, ApiDevice device) {
    Log newLog = new Log();

    newLog.setObjectId(device.getId());
    newLog.setType(LogType.DEVICE_REGISTERED);
    newLog.setUserId(user.getId());
    newLog.setTimestamp(LocalDateTime.now());
    newLog.setNewData(device.toLogObject());

    return newLog;
  }

  public static void editDeviceAndLog(
      ApiUser user,
      ApiDevice oldDevice,
      ApiDevice newDevice,
      DeviceRepository deviceRepository,
      LogRepository logRepository) {
    deviceRepository.save(newDevice.toDatabase());

    Log editDeviceLog = createEditDeviceLog(user, oldDevice, newDevice);
    logRepository.save(editDeviceLog);
  }

  private static Log createEditDeviceLog(ApiUser user, ApiDevice oldDevice, ApiDevice newDevice) {
    Log newLog = new Log();

    newLog.setObjectId(newDevice.getId());
    newLog.setType(LogType.DEVICE_EDITED);
    newLog.setUserId(user.getId());
    newLog.setTimestamp(LocalDateTime.now());
    newLog.setOldData(oldDevice.toLogObject());
    newLog.setNewData(newDevice.toLogObject());

    return newLog;
  }

  public static void addLocationAndLog(
      ApiUser user,
      ApiLocation location,
      LocationRepository locationRepository,
      LogRepository logRepository) {
    locationRepository.save(location.toDatabase());

    Log addLocationLog = createAddLocationLog(user, location);
    logRepository.save(addLocationLog);
  }

  private static Log createAddLocationLog(ApiUser user, ApiLocation location) {
    Log newLog = new Log();

    newLog.setObjectId(location.getId());
    newLog.setType(LogType.LOCATION_ADDED);
    newLog.setUserId(user.getId());
    newLog.setNewData(location.toLogObject());

    return newLog;
  }

  public static void editLocationAndLog(
      ApiUser user,
      ApiLocation oldLocation,
      ApiLocation newLocation,
      LocationRepository locationRepository,
      LogRepository logRepository) {
    locationRepository.save(newLocation.toDatabase());

    Log editLocationLog = createEditLocationLog(user, oldLocation, newLocation);
    logRepository.save(editLocationLog);
  }

  private static Log createEditLocationLog(
      ApiUser user,
      ApiLocation oldLocation,
      ApiLocation newLocation) {
    Log newLog = new Log();

    newLog.setObjectId(newLocation.getId());
    newLog.setType(LogType.LOCATION_EDITED);
    newLog.setUserId(user.getId());
    newLog.setOldData(oldLocation.toLogObject());
    newLog.setNewData(newLocation.toLogObject());

    return newLog;
  }
}
