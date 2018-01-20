package io.lovelacetech.server.command.device;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.device.DeviceApiResponse;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.Messages;
import io.lovelacetech.server.util.RepositoryUtils;

public class SaveDeviceCommand extends DeviceCommand<SaveDeviceCommand> {
  private ApiDevice device;
  private ApiUser user;
  private LocationRepository locationRepository;

  public SaveDeviceCommand setDevice(ApiDevice device) {
    this.device = device;
    return this;
  }

  public SaveDeviceCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public SaveDeviceCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && device != null
        && user != null;
  }

  @Override
  public DeviceApiResponse execute() {
    if (!checkCommand()) {
      return new DeviceApiResponse().setDefault();
    }

    Device deviceUpdate = device.toDatabase();
    if (deviceUpdate.hasId()) {
      Device existingDevice = getDeviceRepository().findOne(deviceUpdate.getId());

      if (existingDevice == null) {
        return new DeviceApiResponse().setNotFound();
      }

      // If the user cannot access this device due to being:
      //   1. AccessLevel USER without permissions for the Device's parent location
      //   2. AccessLevel ADMIN trying to modify a device for a location belonging to
      //      another company
      // then return FORBIDDEN
      if (!AccessUtils.userCanAccessDevice(user, existingDevice, locationRepository)) {
        return new DeviceApiResponse().setAccessDenied();
      }

      existingDevice.applyUpdate(deviceUpdate);
      deviceUpdate = existingDevice;
    }

    if (!deviceUpdate.toApi().isValid()) {
      return new DeviceApiResponse().setInvalidBody();
    }

    Device existingDeviceWithDeviceCode =
        getDeviceRepository().findOneByDeviceCode(deviceUpdate.getDeviceCode());
    if (existingDeviceWithDeviceCode != null
      && RepositoryUtils.updateConflictsWithExistingRow(deviceUpdate, existingDeviceWithDeviceCode)) {
      return new DeviceApiResponse()
          .setConflict()
          .setMessage(Messages.DEVICE_CONFLICTING_DEVICE_CODE);
    }

    deviceUpdate = getDeviceRepository().save(deviceUpdate);

    return new DeviceApiResponse()
        .setSuccess()
        .setResponse(deviceUpdate.toApi());
  }
}
