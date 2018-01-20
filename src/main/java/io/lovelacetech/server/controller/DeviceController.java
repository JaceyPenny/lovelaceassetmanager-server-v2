package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.device.DeviceByLocationIdCommand;
import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.device.DeviceListApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/secure/devices")
public class DeviceController extends BaseController {

  @Autowired
  AssetRepository assetRepository;

  @Autowired
  DeviceRepository deviceRepository;

  @Autowired
  LocationRepository locationRepository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public DeviceListApiResponse getDevices(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new DeviceListApiResponse()
        .setSuccess()
        .setResponse(deviceRepository.findAll());
  }

  @RequestMapping(value = "/forLocationId/{locationId}", method = RequestMethod.GET)
  public DeviceListApiResponse getDevicesForLocationId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID locationId,
      @RequestParam(defaultValue = "true") boolean filled) {
    Location location = locationRepository.findOne(locationId);
    if (!AccessUtils.userCanAccessLocation(authenticatedUser, location)) {
      throw new AccessDeniedException(Messages.ACCESS_DENIED);
    }

    return new DeviceByLocationIdCommand()
        .setDeviceRepository(deviceRepository)
        .setLocationId(locationId)
        .setFilled(filled)
        .setAssetRepository(assetRepository)
        .execute();
  }
}
