package io.lovelacetech.server.command.asset;

import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.asset.AssetApiResponse;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.UUID;

public class DeleteAssetCommand extends AssetCommand<DeleteAssetCommand> {
  private ApiUser user;
  private UUID assetId;

  private LocationRepository locationRepository;
  private DeviceRepository deviceRepository;

  public DeleteAssetCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public DeleteAssetCommand setAssetId(UUID assetId) {
    this.assetId = assetId;
    return this;
  }

  public DeleteAssetCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  public DeleteAssetCommand setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && UUIDUtils.isValidId(assetId)
        && locationRepository != null
        && deviceRepository != null;
  }

  public AssetApiResponse execute() {
    if (!checkCommand()) {
      return new AssetApiResponse().setDefault();
    }

    Asset deletedAsset = getAssetRepository().findOne(assetId);
    if (deletedAsset == null) {
      return new AssetApiResponse().setNotFound();
    }

    if (!AccessUtils.userCanAccessAsset(user, deletedAsset, deviceRepository, locationRepository)) {
      return new AssetApiResponse().setAccessDenied();
    }

    getAssetRepository().delete(deletedAsset);
    return new AssetApiResponse()
        .setSuccess()
        .setResponse(deletedAsset.toApi());
  }
}
