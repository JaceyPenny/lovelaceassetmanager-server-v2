package io.lovelacetech.server.command.device;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.device.DeviceApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.repository.LogRepository;
import io.lovelacetech.server.util.*;

import java.util.UUID;

public class DeleteDeviceCommand extends DeviceCommand<DeleteDeviceCommand> {
  private ApiUser user;
  private UUID deviceId;

  private LocationRepository locationRepository;
  private AssetRepository assetRepository;
  private LogRepository logRepository;

  public DeleteDeviceCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public DeleteDeviceCommand setDeviceId(UUID deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  public DeleteDeviceCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  public DeleteDeviceCommand setAssetRepository(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  public DeleteDeviceCommand setLogRepository(LogRepository logRepository) {
    this.logRepository = logRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && UUIDUtils.isValidId(deviceId)
        && locationRepository != null
        && assetRepository != null
        && logRepository != null;
  }

  public DeviceApiResponse execute() {
    if (!checkCommand()) {
      return new DeviceApiResponse().setDefault();
    }

    if (!AuthenticationUtils.userIsAtLeast(user, AccessLevel.ADMIN)) {
      return new DeviceApiResponse().setAccessDenied();
    }

    Device deletedDevice = getDeviceRepository().findOne(deviceId);
    if (deletedDevice == null) {
      return new DeviceApiResponse().setNotFound();
    }

    if (!AccessUtils.userCanAccessDevice(user, deletedDevice, locationRepository)) {
      return new DeviceApiResponse().setAccessDenied();
    }

    int assetsWithHomeId = assetRepository.countAllByHomeId(deviceId);
    if (assetsWithHomeId > 0) {
      return new DeviceApiResponse().setCannotModify().setMessage("You must change the \"home\" of all the assets in this Device to another Device before deleting.");
    }

    ApiDevice device = deletedDevice.toApi();
    LoaderUtils.populateDevice(device, assetRepository);
    LogUtils.deleteDeviceAndLog(
        user, device, getDeviceRepository(), assetRepository, logRepository, false);
    return new DeviceApiResponse()
        .setSuccess()
        .setResponse(deletedDevice.toApi());
  }
}
