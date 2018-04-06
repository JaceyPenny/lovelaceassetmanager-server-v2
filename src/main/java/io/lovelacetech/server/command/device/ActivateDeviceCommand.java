package io.lovelacetech.server.command.device;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.model.ApiDeviceActivation;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.device.DeviceApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.repository.LogRepository;
import io.lovelacetech.server.util.*;
import org.assertj.core.util.Strings;

import java.util.UUID;

public class ActivateDeviceCommand extends DeviceCommand<ActivateDeviceCommand> {
  private ApiDeviceActivation deviceActivation;
  private LocationRepository locationRepository;
  private ApiUser user;

  private AssetRepository assetRepository;
  private LogRepository logRepository;

  public ActivateDeviceCommand setDeviceActivation(ApiDeviceActivation deviceActivation) {
    this.deviceActivation = deviceActivation;
    return this;
  }

  public ActivateDeviceCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  public ActivateDeviceCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public ActivateDeviceCommand setAssetRepository(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  public ActivateDeviceCommand setLogRepository(LogRepository logRepository) {
    this.logRepository = logRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && deviceActivation != null
        && locationRepository != null
        && assetRepository != null
        && logRepository != null;
  }

  @Override
  public DeviceApiResponse execute() {
    if (!checkCommand()) {
      return new DeviceApiResponse().setDefault();
    }

    if (!AuthenticationUtils.userIsAtLeast(user, AccessLevel.ADMIN)) {
      return new DeviceApiResponse()
          .setAccessDenied();
    }

    String deviceCode = deviceActivation.getDeviceCode();
    UUID locationId = deviceActivation.getLocationId();
    Device existingDeviceWithCode = getDeviceRepository().findOneByDeviceCode(deviceCode);

    if (existingDeviceWithCode == null) {
      return new DeviceApiResponse()
          .setNotFound()
          .setMessage(Messages.DEVICE_NO_DEVICE_CODE);
    }

    if (UUIDUtils.isValidId(existingDeviceWithCode.getLocationId())) {
      return new DeviceApiResponse()
          .setConflict()
          .setMessage(Messages.DEVICE_ALREADY_ACTIVATED);
    }

    Location targetLocation = locationRepository.findOne(locationId);

    if (targetLocation == null) {
      return new DeviceApiResponse()
          .setNotFound()
          .setMessage(Messages.NO_LOCATION_FOUND_WITH_ID);
    }

    if (!UUIDUtils.idsEqual(targetLocation.getCompanyId(), user.getCompanyId())) {
      return new DeviceApiResponse()
          .setAccessDenied();
    }

    if (!Strings.isNullOrEmpty(deviceActivation.getName())) {
      existingDeviceWithCode.setName(deviceActivation.getName());
    } else {
      existingDeviceWithCode.setName("Device " + deviceActivation.getDeviceCode());
    }

    existingDeviceWithCode.setLocationId(locationId);

    Device existingDeviceWithNameAndLocationId = getDeviceRepository().findOneByNameAndLocationId(
        existingDeviceWithCode.getName(), existingDeviceWithCode.getLocationId());
    if (existingDeviceWithNameAndLocationId != null) {
      return new DeviceApiResponse()
          .setConflict()
          .setMessage(Messages.DEVICE_CANNOT_SHARE_NAME);
    }

    existingDeviceWithCode = LogUtils.registerDeviceAndLog(
        user, existingDeviceWithCode.toApi(), getDeviceRepository(), logRepository);

    ApiDevice existingApiDeviceWithCode = existingDeviceWithCode.toApi();

    LoaderUtils.fillAssetCounts(existingApiDeviceWithCode, assetRepository);

    return new DeviceApiResponse()
        .setSuccess()
        .setResponse(existingApiDeviceWithCode);
  }
}
