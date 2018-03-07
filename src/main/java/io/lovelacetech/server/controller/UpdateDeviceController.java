package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.deviceupdate.DeviceUpdateCommand;
import io.lovelacetech.server.model.api.enums.UpdateDeviceResponse;
import io.lovelacetech.server.model.api.model.ApiDeviceUpdate;
import io.lovelacetech.server.repository.*;
import io.lovelacetech.server.service.UpdateDeviceValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/deviceUpdate")
public class UpdateDeviceController {
  private final CompanyRepository companyRepository;
  private final LocationRepository locationRepository;
  private final DeviceRepository deviceRepository;
  private final AssetRepository assetRepository;
  private final LogRepository logRepository;
  private final AssetTypeRepository assetTypeRepository;

  private final UpdateDeviceValidationService updateDeviceValidationService;

  @Autowired
  public UpdateDeviceController(
      CompanyRepository companyRepository,
      LocationRepository locationRepository,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository,
      LogRepository logRepository,
      AssetTypeRepository assetTypeRepository,
      UpdateDeviceValidationService updateDeviceValidationService) {
    this.companyRepository = companyRepository;
    this.locationRepository = locationRepository;
    this.deviceRepository = deviceRepository;
    this.assetRepository = assetRepository;
    this.logRepository = logRepository;
    this.assetTypeRepository = assetTypeRepository;
    this.updateDeviceValidationService = updateDeviceValidationService;
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  public UpdateDeviceResponse updateDeviceWithRfidTags(
      @RequestParam(name = "deviceCode") String deviceCode,
      @RequestParam(name = "time") long unixTime,
      @RequestParam(name = "hash") String hash,
      @RequestBody ApiDeviceUpdate deviceUpdate) {
    UpdateDeviceResponse response = updateDeviceValidationService
        .checkRequestValidity("POSTapideviceUpdate/", deviceCode, unixTime, hash);
    if (response != UpdateDeviceResponse.SUCCESS) {
      return response;
    }

    System.out.println(deviceUpdate);

    return new DeviceUpdateCommand()
        .setDeviceCode(deviceCode)
        .setRfids(deviceUpdate.getRfidTags())
        .setCompanyRepository(companyRepository)
        .setLocationRepository(locationRepository)
        .setAssetRepository(assetRepository)
        .setDeviceRepository(deviceRepository)
        .setLogRepository(logRepository)
        .setAssetTypeRepository(assetTypeRepository)
        .execute();
  }
}
