package io.lovelacetech.server.command.asset;

import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.asset.AssetApiResponse;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.UUID;

public class AssetByAssetIdCommand extends AssetCommand<AssetByAssetIdCommand> {
  private ApiUser user;
  private UUID assetId;

  private DeviceRepository deviceRepository;
  private LocationRepository locationRepository;

  public AssetByAssetIdCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public AssetByAssetIdCommand setAssetId(UUID assetId) {
    this.assetId = assetId;
    return this;
  }

  public AssetByAssetIdCommand setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return this;
  }

  public AssetByAssetIdCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && UUIDUtils.isValidId(assetId)
        && deviceRepository != null
        && locationRepository != null;
  }

  @Override
  public AssetApiResponse execute() {
    if (!checkCommand()) {
      return new AssetApiResponse().setDefault();
    }

    Asset asset = getAssetRepository().findOne(assetId);
    if (asset == null) {
      return new AssetApiResponse().setNotFound();
    }

    if (!AuthenticationUtils.userIsSuper(user)
        && !AccessUtils.userCanAccessAsset(user, asset, deviceRepository, locationRepository)) {
      return new AssetApiResponse().setAccessDenied();
    }

    return new AssetApiResponse()
        .setSuccess()
        .setResponse(asset.toApi());
  }
}
