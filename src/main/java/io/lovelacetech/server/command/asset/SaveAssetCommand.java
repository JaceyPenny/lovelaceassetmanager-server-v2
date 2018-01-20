package io.lovelacetech.server.command.asset;

import com.google.common.base.Strings;
import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.asset.AssetApiResponse;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.Messages;
import io.lovelacetech.server.util.RepositoryUtils;

public class SaveAssetCommand extends AssetCommand<SaveAssetCommand> {
  private ApiAsset asset;
  private ApiUser user;
  private DeviceRepository deviceRepository;
  private LocationRepository locationRepository;

  public SaveAssetCommand setAsset(ApiAsset asset) {
    this.asset = asset;
    return this;
  }

  public SaveAssetCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public SaveAssetCommand setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return this;
  }

  public SaveAssetCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && asset != null
        && deviceRepository != null
        && locationRepository != null;
  }

  @Override
  public AssetApiResponse execute() {
    if (!checkCommand()) {
      return new AssetApiResponse().setDefault();
    }

    Asset assetUpdate = asset.toDatabase();
    if (assetUpdate.hasId()) {
      Asset existingAsset = getAssetRepository().findOne(assetUpdate.getId());

      if (existingAsset == null) {
        return new AssetApiResponse().setNotFound();
      }

      // If the user cannot access this asset due to being:
      //   1. AccessLevel USER without permissions for the Asset's parent device (homeId)
      //   2. AccessLevel ADMIN trying to modify an asset for a device belonging to another company
      // then return FORBIDDEN
      if (!AccessUtils.userCanAccessAsset(
          user, existingAsset, deviceRepository, locationRepository)) {
        return new AssetApiResponse().setAccessDenied();
      }

      // Check if the user is attempting to modify the asset's RFID tag. This is not permitted
      if (!Strings.isNullOrEmpty(assetUpdate.getRfid())
          && !Strings.isNullOrEmpty(existingAsset.getRfid())
          && !assetUpdate.getRfid().equals(existingAsset.getRfid())) {
        return new AssetApiResponse().setCannotModify();
      }

      existingAsset.applyUpdate(assetUpdate);
      assetUpdate = existingAsset;
    }

    if (!assetUpdate.toApi().isValid()) {
      return new AssetApiResponse().setInvalidBody();
    }

    Asset existingAssetWithRFID = getAssetRepository().findOneByRfid(assetUpdate.getRfid());
    if (existingAssetWithRFID != null
      && RepositoryUtils.updateConflictsWithExistingRow(assetUpdate, existingAssetWithRFID)) {
      return new AssetApiResponse()
          .setConflict()
          .setMessage(Messages.ASSET_CONFLICTING_RFID);
    }

    assetUpdate = getAssetRepository().save(assetUpdate);

    return new AssetApiResponse()
        .setSuccess()
        .setResponse(assetUpdate.toApi());
  }
}
