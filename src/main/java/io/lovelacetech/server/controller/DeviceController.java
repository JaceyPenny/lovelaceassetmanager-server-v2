package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.device.ActivateDeviceCommand;
import io.lovelacetech.server.command.device.DeviceByDeviceIdCommand;
import io.lovelacetech.server.command.device.DeviceByLocationIdCommand;
import io.lovelacetech.server.command.device.SaveDeviceCommand;
import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.model.ApiDeviceActivation;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.device.DeviceApiResponse;
import io.lovelacetech.server.model.api.response.device.DeviceListApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.Messages;
import io.lovelacetech.server.util.UUIDUtils;
import javassist.NotFoundException;
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

  @RequestMapping(value = "/byDeviceId/{deviceId}", method = RequestMethod.GET)
  public DeviceApiResponse getDeviceByDeviceId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID deviceId,
      @RequestParam(defaultValue = "false") boolean filled) {
    return new DeviceByDeviceIdCommand()
        .setDeviceRepository(deviceRepository)
        .setLocationRepository(locationRepository)
        .setAssetRepository(assetRepository)
        .setDeviceId(deviceId)
        .setUser(authenticatedUser)
        .setFilled(filled)
        .execute();
  }

  @RequestMapping(value = "/byLocationId/{locationId}", method = RequestMethod.GET)
  public DeviceListApiResponse getDevicesByLocationId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID locationId,
      @RequestParam(defaultValue = "true") boolean filled) {
    if (!AccessUtils.userCanAccessLocation(authenticatedUser, locationId, locationRepository)) {
      return new DeviceListApiResponse().setAccessDenied();
    }

    return new DeviceByLocationIdCommand()
        .setDeviceRepository(deviceRepository)
        .setLocationId(locationId)
        .setFilled(filled)
        .setAssetRepository(assetRepository)
        .execute();
  }

  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.POST)
  public DeviceApiResponse putDeviceForAuthenticated(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody ApiDevice device) {
    return new SaveDeviceCommand()
        .setDeviceRepository(deviceRepository)
        .setLocationRepository(locationRepository)
        .setUser(authenticatedUser)
        .setDevice(device)
        .execute();
  }

  @RequestMapping(value = "/activateDevice", method = RequestMethod.POST)
  public DeviceApiResponse activateDeviceWithCode(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody ApiDeviceActivation deviceActivation) {
    return new ActivateDeviceCommand()
        .setDeviceRepository(deviceRepository)
        .setLocationRepository(locationRepository)
        .setUser(authenticatedUser)
        .setDeviceActivation(deviceActivation)
        .execute();
  }
}
