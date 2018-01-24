package io.lovelacetech.server.command.device;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiDeviceActivation;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.device.DeviceApiResponse;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.Messages;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.UUID;

public class ActivateDeviceCommand extends DeviceCommand<ActivateDeviceCommand> {
  private ApiDeviceActivation deviceActivation;
  private LocationRepository locationRepository;
  private ApiUser user;

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

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && deviceActivation != null
        && locationRepository != null;
  }

  @Override
  public DeviceApiResponse execute() {
    if (!checkCommand()) {
      return new DeviceApiResponse().setDefault();
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

    if (!AuthenticationUtils.userIsAtLeast(user, AccessLevel.ADMIN)) {
      return new DeviceApiResponse()
          .setAccessDenied();
    }

    existingDeviceWithCode.setLocationId(locationId);
    existingDeviceWithCode = getDeviceRepository().save(existingDeviceWithCode);

    return new DeviceApiResponse()
        .setSuccess()
        .setResponse(existingDeviceWithCode.toApi());
  }
}
