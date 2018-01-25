package io.lovelacetech.server.command.asset;

import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.api.model.ApiAssetList;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.asset.AssetListApiResponse;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.List;
import java.util.UUID;

public class AssetByDeviceIdCommand extends AssetCommand<AssetByDeviceIdCommand> {
  private UUID deviceId;
  private ApiUser user;
  private DeviceRepository deviceRepository;
  private LocationRepository locationRepository;

  public AssetByDeviceIdCommand setDeviceId(UUID deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  public AssetByDeviceIdCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public AssetByDeviceIdCommand setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return this;
  }

  public AssetByDeviceIdCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && UUIDUtils.isValidId(deviceId)
        && user != null
        && deviceRepository != null
        && locationRepository != null;
  }

  @Override
  public AssetListApiResponse execute() {
    if (!checkCommand()) {
      return new AssetListApiResponse().setDefault();
    }

    if (!AccessUtils.userCanAccessDevice(
        user, deviceId, deviceRepository, locationRepository)) {
      return new AssetListApiResponse().setAccessDenied();
    }

    List<ApiAsset> assets = RepositoryUtils.toApiList(
        getAssetRepository().findAllByDeviceId(deviceId));

    return new AssetListApiResponse()
        .setSuccess()
        .setResponse(new ApiAssetList().setAssets(assets));
  }
}
