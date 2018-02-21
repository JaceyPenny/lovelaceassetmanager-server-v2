package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.api.enums.UpdateDeviceResponse;
import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.model.ApiDeviceUpdate;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LogRepository;
import io.lovelacetech.server.service.UpdateDeviceValidationService;
import io.lovelacetech.server.util.ListUtil;
import io.lovelacetech.server.util.LoaderUtils;
import io.lovelacetech.server.util.LogUtil;
import io.lovelacetech.server.util.RepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/deviceUpdate")
public class UpdateDeviceController {
  private final DeviceRepository deviceRepository;
  private final AssetRepository assetRepository;
  private final LogRepository logRepository;

  private final UpdateDeviceValidationService updateDeviceValidationService;

  @Autowired
  public UpdateDeviceController(
      DeviceRepository deviceRepository,
      AssetRepository assetRepository,
      LogRepository logRepository,
      UpdateDeviceValidationService updateDeviceValidationService) {
    this.deviceRepository = deviceRepository;
    this.assetRepository = assetRepository;
    this.logRepository = logRepository;
    this.updateDeviceValidationService = updateDeviceValidationService;
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  public UpdateDeviceResponse updateDeviceWithRfidTags(
      @RequestParam(name = "deviceCode") String deviceCode,
      @RequestParam(name = "time") long unixTime,
      @RequestParam(name = "hash") String hash,
      @RequestBody ApiDeviceUpdate deviceUpdate) {
    UpdateDeviceResponse response = updateDeviceValidationService
        .checkRequestValidity("POST/api/deviceUpdate/", deviceCode, unixTime, hash);
    if (response != UpdateDeviceResponse.SUCCESS) {
      return response;
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
    List<String> newRfidTags = deviceUpdate.getRfidTags();

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
