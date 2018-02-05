package io.lovelacetech.server.command.asset;

import com.google.common.base.Strings;
import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.AssetType;
import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.enums.AssetStatus;
import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.api.model.ApiAssetType;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.asset.AssetApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.AssetTypeRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.Messages;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;
import org.h2.util.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;

public class SaveAssetCommand extends AssetCommand<SaveAssetCommand> {
  private ApiAsset asset;
  private ApiUser user;
  private AssetTypeRepository assetTypeRepository;
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

  public SaveAssetCommand setAssetTypeRepository(AssetTypeRepository assetTypeRepository) {
    this.assetTypeRepository = assetTypeRepository;
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
        && assetTypeRepository != null
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

    if (Strings.isNullOrEmpty(assetUpdate.getName())) {
      assetUpdate.setName("{NEW ASSET}");
    }

    if (assetUpdate.getStatus() == null) {
      assetUpdate.setStatus(AssetStatus.AVAILABLE);
    }

    if (!assetUpdate.toApi().isValid()) {
      return new AssetApiResponse().setInvalidBody();
    }

    if (!UUIDUtils.isValidId(assetUpdate.getDeviceId())
        && !UUIDUtils.isValidId(assetUpdate.getLocationId())) {
      Device parentDevice = deviceRepository.findOne(assetUpdate.getHomeId());
      if (parentDevice == null) {
        return new AssetApiResponse().setNotFound();
      }

      assetUpdate.setLocationId(parentDevice.getLocationId());
    }

    Asset existingAssetWithRFID = getAssetRepository().findOneByRfid(assetUpdate.getRfid());
    if (existingAssetWithRFID != null
      && RepositoryUtils.updateConflictsWithExistingRow(assetUpdate, existingAssetWithRFID)) {
      return new AssetApiResponse()
          .setConflict()
          .setMessage(Messages.ASSET_CONFLICTING_RFID);
    }

    if (assetUpdate.getAssetType() == null) {
      assetUpdate.setAssetType(ApiAssetType.getDefault().toDatabase());
    }

    if (!UUIDUtils.isValidId(assetUpdate.getAssetType().getCompanyId())) {
      assetUpdate.getAssetType().setCompanyId(user.getCompanyId());
    }

    if (assetUpdate.getAssetType().getType().equals("")) {
      assetUpdate.getAssetType().setType(ApiAssetType.DEFAULT_ASSET_TYPE);
    }

    if (!assetUpdate.getAssetType().toApi().isValid()) {
      AssetType assetType = assetTypeRepository.findOneByCompanyIdAndType(
          user.getCompanyId(), assetUpdate.getAssetType().getType());

      if (assetType == null) {
        if (!assetUpdate.getAssetType().getType().equals(ApiAssetType.DEFAULT_ASSET_TYPE)
            && user.getAccessLevel() == AccessLevel.USER) {
          return new AssetApiResponse().setAccessDenied().setMessage(Messages.ASSET_CANNOT_CREATE_NEW_ASSET_TYPE);
        }

        assetType = assetTypeRepository.save(assetUpdate.getAssetType());
      }

      assetUpdate.setAssetType(assetType);
    }

    try {
      assetUpdate = getAssetRepository().save(assetUpdate);
    } catch (DataIntegrityViolationException constraintViolationException) {
      return new AssetApiResponse().setBadId();
    }

    return new AssetApiResponse()
        .setSuccess()
        .setResponse(assetUpdate.toApi());
  }
}
