package io.lovelacetech.server.command.device;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.device.DeviceApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.repository.LogRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.LogUtil;
import io.lovelacetech.server.util.UUIDUtils;

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

    if (user.getAccessLevel() == AccessLevel.USER) {
      return new DeviceApiResponse().setAccessDenied();
    }

    Device deletedDevice = getDeviceRepository().findOne(deviceId);
    if (deletedDevice == null) {
      return new DeviceApiResponse().setNotFound();
    }

    if (!AccessUtils.userCanAccessDevice(user, deletedDevice, locationRepository)) {
      return new DeviceApiResponse().setAccessDenied();
    }

    int assetsInDevice = assetRepository.countAllByDeviceId(deviceId);
    if (assetsInDevice > 0) {
      return new DeviceApiResponse().setCannotModify();
    }

    int assetsWithHomeId = assetRepository.countAllByHomeId(deviceId);
    if (assetsWithHomeId > 0) {
      return new DeviceApiResponse().setCannotModify();
    }

    LogUtil.deleteDeviceAndLog(deletedDevice.toApi(), getDeviceRepository(), logRepository, false);
    return new DeviceApiResponse()
        .setSuccess()
        .setResponse(deletedDevice.toApi());
  }
}
