package io.lovelacetech.server.util;

import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.Log;
import io.lovelacetech.server.model.api.enums.LogType;
import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.repository.AssetRepository;
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
}
