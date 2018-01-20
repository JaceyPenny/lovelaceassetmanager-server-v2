package io.lovelacetech.server.command.device;

import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.model.ApiDeviceList;
import io.lovelacetech.server.model.api.response.device.DeviceListApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.util.LoaderUtils;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.List;
import java.util.UUID;

public class DeviceByLocationIdCommand extends DeviceCommand<DeviceByLocationIdCommand> {
  private UUID locationId;
  private boolean filled;
  private AssetRepository assetRepository;

  public DeviceByLocationIdCommand setLocationId(UUID locationId) {
    this.locationId = locationId;
    return this;
  }

  public DeviceByLocationIdCommand setFilled(boolean filled) {
    this.filled = filled;
    return this;
  }

  public DeviceByLocationIdCommand setAssetRepository(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && UUIDUtils.isValidId(locationId)
        && (!filled || assetRepository != null);
  }

  @Override
  public DeviceListApiResponse execute() {
    if (!checkCommand()) {
      return new DeviceListApiResponse().setDefault();
    }

    List<ApiDevice> devices = RepositoryUtils.toApiList(
        getDeviceRepository().findAllByLocationId(locationId));

    if (filled) {
      LoaderUtils.populateDevices(devices, assetRepository);
    }

    return new DeviceListApiResponse()
        .setSuccess()
        .setResponse(new ApiDeviceList().setDevices(devices));
  }
}
