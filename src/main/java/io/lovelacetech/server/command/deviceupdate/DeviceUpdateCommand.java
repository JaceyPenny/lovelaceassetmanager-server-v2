package io.lovelacetech.server.command.deviceupdate;

import com.google.common.base.Strings;
import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.api.enums.UpdateDeviceResponse;
import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.api.model.ApiAssetType;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.repository.*;
import io.lovelacetech.server.util.ListUtils;
import io.lovelacetech.server.util.LoaderUtils;
import io.lovelacetech.server.util.LogUtils;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceUpdateCommand {
  private String deviceCode;
  private List<String> rfids;

  private CompanyRepository companyRepository;
  private LocationRepository locationRepository;
  private DeviceRepository deviceRepository;
  private AssetRepository assetRepository;
  private LogRepository logRepository;
  private AssetTypeRepository assetTypeRepository;

  public DeviceUpdateCommand setDeviceCode(String deviceCode) {
    this.deviceCode = deviceCode;
    return this;
  }

  public DeviceUpdateCommand setRfids(List<String> rfids) {
    this.rfids = rfids;
    return this;
  }

  public DeviceUpdateCommand setCompanyRepository(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
    return this;
  }

  public DeviceUpdateCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
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

  public DeviceUpdateCommand setAssetTypeRepository(AssetTypeRepository assetTypeRepository) {
    this.assetTypeRepository = assetTypeRepository;
    return this;
  }

  public boolean checkCommand() {
    return !Strings.isNullOrEmpty(deviceCode)
        && rfids != null
        && companyRepository != null
        && locationRepository != null
        && assetRepository != null
        && deviceRepository != null
        && logRepository != null
        && assetTypeRepository != null;
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
    List<String> newRfidTags = rfids.stream().map(String::toUpperCase).collect(Collectors.toList());

    // Use list subtraction to figure out which assets are new to the device and which have been
    // removed.
    List<String> newlyAddedRfids = ListUtils.subtract(newRfidTags, currentRfidTags);
    List<String> removedRfids = ListUtils.subtract(currentRfidTags, newRfidTags);

    List<ApiAsset> newlyAddedAssets = RepositoryUtils
        .toApiList(assetRepository.findAllByRfidIn(newlyAddedRfids));
    List<ApiAsset> removedAssets = LoaderUtils.filterByRfidIn(apiDevice.getAssets(), removedRfids);

    List<String> rfidsWithoutAssets = new ArrayList<>();
    for (String newlyAddedRfid : newlyAddedRfids) {
      if (newlyAddedAssets.stream().noneMatch((asset) -> asset.getRfid().equals(newlyAddedRfid))) {
        rfidsWithoutAssets.add(newlyAddedRfid);
      }
    }

    if (!rfidsWithoutAssets.isEmpty()) {
      ApiCompany company = LoaderUtils.getCompanyForDevice(
          apiDevice, locationRepository, companyRepository);

      if (company == null) {
        return UpdateDeviceResponse.UNKNOWN;
      }

      // create assets for all the brand new rfids
      ApiAssetType assetType = LoaderUtils.getOrCreateDefaultAssetType(
          company.getId(), assetTypeRepository);
      List<ApiAsset> assets = LoaderUtils.generateAssetsForRfids(
          rfidsWithoutAssets, apiDevice.getId(), assetType);
      assets = RepositoryUtils.toApiList(
          assetRepository.save(RepositoryUtils.toDatabaseList(assets)));

      newlyAddedAssets.addAll(assets);
    }

    // Update the assets and create logs.
    LogUtils.removeAssetsFromDeviceAndLog(
        removedAssets,
        apiDevice.getId(),
        apiDevice.getLocationId(),
        assetRepository,
        logRepository);

    LogUtils.addAssetsToDeviceAndLog(
        newlyAddedAssets,
        apiDevice.getLocationId(),
        apiDevice.getId(),
        assetRepository,
        logRepository);

    return UpdateDeviceResponse.SUCCESS;
  }
}
