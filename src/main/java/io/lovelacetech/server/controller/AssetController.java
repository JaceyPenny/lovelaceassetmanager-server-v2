package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.asset.AssetByDeviceIdCommand;
import io.lovelacetech.server.command.asset.AssetByHomeIdCommand;
import io.lovelacetech.server.command.asset.AssetByLocationIdCommand;
import io.lovelacetech.server.command.asset.SaveAssetCommand;
import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.asset.AssetApiResponse;
import io.lovelacetech.server.model.api.response.asset.AssetListApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/secure/assets")
public class AssetController extends BaseController {

  @Autowired
  LocationRepository locationRepository;

  @Autowired
  DeviceRepository deviceRepository;

  @Autowired
  AssetRepository assetRepository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public AssetListApiResponse getAssets(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new AssetListApiResponse()
        .setSuccess()
        .setResponse(assetRepository.findAll());
  }

  @RequestMapping(value = "/currentlyInDeviceId/{deviceId}", method = RequestMethod.GET)
  public AssetListApiResponse getAssetsInDeviceByDeviceId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID deviceId) {
    if (!AccessUtils.userCanAccessDevice(
        authenticatedUser, deviceId, deviceRepository, locationRepository)) {
      return new AssetListApiResponse().setAccessDenied();
    }

    return new AssetByDeviceIdCommand()
        .setAssetRepository(assetRepository)
        .setDeviceId(deviceId)
        .execute();
  }

  @RequestMapping(value = "/currentlyInLocationId/{locationId}", method = RequestMethod.GET)
  public AssetListApiResponse getAssetsInLocationByLocationId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID locationId) {
    if (!AccessUtils.userCanAccessLocation(authenticatedUser, locationId, locationRepository)) {
      return new AssetListApiResponse().setAccessDenied();
    }

    return new AssetByLocationIdCommand()
        .setAssetRepository(assetRepository)
        .setLocationId(locationId)
        .execute();
  }

  @RequestMapping(value = "/belongingToDeviceId/{deviceId}", method = RequestMethod.GET)
  public AssetListApiResponse getAssetsBelongingToDeviceId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID deviceId) {
    if (!AccessUtils.userCanAccessDevice(
        authenticatedUser, deviceId, deviceRepository, locationRepository)) {
      return new AssetListApiResponse().setAccessDenied();
    }

    return new AssetByHomeIdCommand()
        .setAssetRepository(assetRepository)
        .setHomeId(deviceId)
        .execute();
  }

  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.POST)
  public AssetApiResponse putAssetForAuthenticated(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody ApiAsset asset) {
    return new SaveAssetCommand()
        .setAssetRepository(assetRepository)
        .setDeviceRepository(deviceRepository)
        .setLocationRepository(locationRepository)
        .setAsset(asset)
        .execute();
  }
}
