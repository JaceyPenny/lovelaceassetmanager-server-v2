package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.location.LocationByLocationIdCommand;
import io.lovelacetech.server.command.location.LocationsByCompanyIdCommand;
import io.lovelacetech.server.command.location.LocationsForUserCommand;
import io.lovelacetech.server.command.location.SaveLocationCommand;
import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.location.LocationApiResponse;
import io.lovelacetech.server.model.api.response.location.LocationListApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
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

  @RequestMapping(value = "/byLocationId/{locationId}", method = RequestMethod.GET)
  public LocationApiResponse getLocationByLocationId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID locationId,
      @RequestParam(defaultValue = "false") boolean filled) {
    checkBelongsToCompany(authenticatedUser);

    return new LocationByLocationIdCommand()
        .setLocationRepository(locationRepository)
        .setDeviceRepository(deviceRepository)
        .setAssetRepository(assetRepository)
        .setUser(authenticatedUser)
        .setLocationId(locationId)
        .setFilled(filled)
        .execute();
  }

  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.GET)
  public LocationListApiResponse getLocationsForAuthenticatedUserFilled(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestParam(defaultValue = "true") boolean filled) {
    checkBelongsToCompany(authenticatedUser);

    return new LocationsForUserCommand()
        .setLocationRepository(locationRepository)
        .setUser(authenticatedUser)
        .setFilled(filled)
        .setAssetRepository(assetRepository)
        .setDeviceRepository(deviceRepository)
        .execute();
  }

  @RequestMapping(value = "/byCompanyId/{companyId}", method = RequestMethod.GET)
  public LocationListApiResponse getLocationsByCompanyId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID companyId,
      @RequestParam(defaultValue = "true") boolean filled) {
    checkIsSuper(authenticatedUser);

    return new LocationsByCompanyIdCommand()
        .setLocationRepository(locationRepository)
        .setCompanyId(companyId)
        .setUser(authenticatedUser)
        .setFilled(filled)
        .setAssetRepository(assetRepository)
        .setDeviceRepository(deviceRepository)
        .execute();
  }

  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.POST)
  public LocationApiResponse putLocationForAuthenticatedAdmin(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody ApiLocation location) {
    checkBelongsToCompany(authenticatedUser);

    return new SaveLocationCommand()
        .setLocationRepository(locationRepository)
        .setLocation(location)
        .setUser(authenticatedUser)
        .execute();
  }
}
