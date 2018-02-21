package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.api.enums.UpdateDeviceResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/deviceUpdate")
public class UpdateDeviceController {
  @Value("${UPDATE_DEVICE_SECRET}")
  private String UPDATE_DEVICE_SECRET;

  private final LocationRepository locationRepository;
  private final DeviceRepository deviceRepository;
  private final AssetRepository assetRepository;

  @Autowired
  public UpdateDeviceController(
      LocationRepository locationRepository,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository) {
    this.locationRepository = locationRepository;
    this.deviceRepository = deviceRepository;
    this.assetRepository = assetRepository;
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  public UpdateDeviceResponse updateDeviceWithRfidTags(
      @RequestParam(name = "deviceCode") String deviceCode,
      @RequestParam(name = "time") long unixTime,
      @RequestParam(name = "hash") String hash) {
    return UpdateDeviceResponse.SUCCESS;
  }
}
