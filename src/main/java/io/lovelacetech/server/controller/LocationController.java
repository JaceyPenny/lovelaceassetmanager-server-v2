package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.location.LocationsForUserCommand;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.location.LocationListApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secure/locations")
public class LocationController extends BaseController {

  @Autowired
  LocationRepository locationRepository;

  @Autowired
  AssetRepository assetRepository;

  @Autowired
  DeviceRepository deviceRepository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public LocationListApiResponse getLocations(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new LocationListApiResponse()
        .setSuccess()
        .setResponse(locationRepository.findAll());
  }

  @RequestMapping(value = "/forAuthenticated/filled", method = RequestMethod.GET)
  public LocationListApiResponse getLocationsForAuthenticatedUserFilled(@RequestAttribute ApiUser authenticatedUser) {
    return new LocationsForUserCommand()
        .setLocationRepository(locationRepository)
        .setUser(authenticatedUser)
        .setFilled(true)
        .setAssetRepository(assetRepository)
        .setDeviceRepository(deviceRepository)
        .execute();
  }

  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.GET)
  public LocationListApiResponse getLocationsForAuthenticatedUser(@RequestAttribute ApiUser authenticatedUser) {
    return new LocationsForUserCommand()
        .setLocationRepository(locationRepository)
        .setUser(authenticatedUser)
        .setFilled(false)
        .execute();
  }
}
