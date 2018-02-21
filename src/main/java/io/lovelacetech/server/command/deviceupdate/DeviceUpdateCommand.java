package io.lovelacetech.server.command.deviceupdate;

import com.google.common.base.Strings;
import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.api.enums.UpdateDeviceResponse;
import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LogRepository;
import io.lovelacetech.server.util.ListUtil;
import io.lovelacetech.server.util.LoaderUtils;
import io.lovelacetech.server.util.LogUtil;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.List;

public class DeviceUpdateCommand {
  private String deviceCode;
  private List<String> rfids;

  private DeviceRepository deviceRepository;
  private AssetRepository assetRepository;
  private LogRepository logRepository;

  public DeviceUpdateCommand setDeviceCode(String deviceCode) {
    this.deviceCode = deviceCode;
    return this;
  }

  public DeviceUpdateCommand setRfids(List<String> rfids) {
    this.rfids = rfids;
    return this;
  }

  public DeviceUpdateCommand setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return this;
  }

  public DeviceUpdateCommand setAssetRepository(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  public DeviceUpdateCommand setLogRepository(LogRepository logRepository) {
    this.logRepository = logRepository;
    return this;
  }

  public boolean checkCommand() {
    return !Strings.isNullOrEmpty(deviceCode)
        && rfids != null
        && assetRepository != null
        && deviceRepository != null
        && logRepository != null;
  }

  public UpdateDeviceResponse execute() {
    if (!checkCommand()) {
      return UpdateDeviceResponse.UNKNOWN;
    }

    Device existingDevice = deviceRepository.findOneByDeviceCode(deviceCode);
    if (existingDevice == null) {
      return UpdateDeviceResponse.BAD_DEVICE_CODE;
    }

    // Populate the device with its current Asset objects
    ApiDevice apiDevice = existingDevice.toApi();
    LoaderUtils.populateDevice(apiDevice, assetRepository);

    List<String> currentRfidTags =
        RepositoryUtils.mapIterable(apiDevice.getAssets(), ApiAsset::getRfid);
    List<String> newRfidTags = rfids;

    // Use list subtraction to figure out which assets are new to the device and which have been
    // removed.
    List<String> newlyAddedRfids = ListUtil.subtract(newRfidTags, currentRfidTags);
    List<String> removedRfids = ListUtil.subtract(currentRfidTags, newRfidTags);

    List<ApiAsset> newlyAddedAssets = RepositoryUtils
        .toApiList(assetRepository.findAllByRfidIn(newlyAddedRfids));
    List<ApiAsset> removedAssets = LoaderUtils.filterByRfidIn(apiDevice.getAssets(), removedRfids);

    // Update the assets and create logs.
    LogUtil.removeAssetsFromDeviceAndLog(
        removedAssets,
        apiDevice.getId(),
        apiDevice.getLocationId(),
        assetRepository,
        logRepository);

    LogUtil.addAssetsToDeviceAndLog(
        newlyAddedAssets,
        apiDevice.getLocationId(),
        apiDevice.getId(),
        assetRepository,
        logRepository);

    return UpdateDeviceResponse.SUCCESS;
  }
}
