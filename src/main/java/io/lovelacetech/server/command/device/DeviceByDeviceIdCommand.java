package io.lovelacetech.server.command.device;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.device.DeviceApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.LoaderUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.UUID;

public class DeviceByDeviceIdCommand extends DeviceCommand<DeviceByDeviceIdCommand> {
  private LocationRepository locationRepository;
  private AssetRepository assetRepository;
  private ApiUser user;
  private UUID deviceId;
  private boolean filled;

  public DeviceByDeviceIdCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  public DeviceByDeviceIdCommand setAssetRepository(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  public DeviceByDeviceIdCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public DeviceByDeviceIdCommand setDeviceId(UUID deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  public DeviceByDeviceIdCommand setFilled(boolean filled) {
    this.filled = filled;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && UUIDUtils.isValidId(deviceId)
        && locationRepository != null
        && (!filled || assetRepository != null);
  }

  @Override
  public DeviceApiResponse execute() {
    if (!checkCommand()) {
      return new DeviceApiResponse().setDefault();
    }

    Device device = getDeviceRepository().findOne(deviceId);
    if (device == null) {
      return new DeviceApiResponse().setNotFound();
    }

    if (!AuthenticationUtils.userIsSuper(user)
        && !AccessUtils.userCanAccessDevice(user, device, locationRepository)) {
      return new DeviceApiResponse().setAccessDenied();
    }

    ApiDevice apiDevice = device.toApi();
    if (filled) {
      LoaderUtils.populateDevice(apiDevice, assetRepository);
    }

    return new DeviceApiResponse()
        .setSuccess()
        .setResponse(apiDevice);
  }
}
