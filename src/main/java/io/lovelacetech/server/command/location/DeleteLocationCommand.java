package io.lovelacetech.server.command.location;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.location.LocationApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LogRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.LogUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.UUID;

public class DeleteLocationCommand extends LocationCommand<DeleteLocationCommand> {
  private ApiUser user;
  private UUID locationId;

  private DeviceRepository deviceRepository;
  private AssetRepository assetRepository;
  private LogRepository logRepository;

  public DeleteLocationCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public DeleteLocationCommand setLocationId(UUID locationId) {
    this.locationId = locationId;
    return this;
  }

  public DeleteLocationCommand setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return this;
  }

  public DeleteLocationCommand setAssetRepository(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  public DeleteLocationCommand setLogRepository(LogRepository logRepository) {
    this.logRepository = logRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && UUIDUtils.isValidId(locationId);
  }

  @Override
  public LocationApiResponse execute() {
    if (!checkCommand()) {
      return new LocationApiResponse().setDefault();
    }

    if (!AuthenticationUtils.userIsAtLeast(user, AccessLevel.ADMIN)) {
      return new LocationApiResponse().setAccessDenied();
    }

    Location deletedLocation = getLocationRepository().findOne(locationId);
    if (deletedLocation == null) {
      return new LocationApiResponse().setNotFound();
    }

    if (!AccessUtils.userCanAccessLocation(user, deletedLocation)) {
      return new LocationApiResponse().setAccessDenied();
    }

    int devicesInLocation = deviceRepository.countAllByLocationId(locationId);
    if (devicesInLocation > 0) {
      return new LocationApiResponse().setCannotModify();
    }

    int assetsInLocation = assetRepository.countAllByLocationId(locationId);
    if (assetsInLocation > 0) {
      return new LocationApiResponse().setCannotModify();
    }

    LogUtils.deleteLocationAndLog(
        user, deletedLocation.toApi(), getLocationRepository(), logRepository);
    return new LocationApiResponse()
        .setSuccess()
        .setResponse(deletedLocation.toApi());
  }
}
