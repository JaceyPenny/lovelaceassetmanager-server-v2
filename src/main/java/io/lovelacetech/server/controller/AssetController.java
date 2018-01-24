package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.asset.*;
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

  /**
   * <b>  GET /api/secure/assets/  </b>
   * <br><br>
   * Gets all the Assets in the database and returns in a list.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "assets": [Asset]
   *   }
   * }
   * }
   * <br><br>
   * <b>  PERMISSIONS  </b><br>
   * User must be SUPER to access this endpoint.
   *
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public AssetListApiResponse getAssets(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new AssetListApiResponse()
        .setSuccess()
        .setResponse(assetRepository.findAll());
  }

  /**
   * <b>  GET /api/secure/assets/byAssetId/{assetId}  </b>
   * <br><br>
   * Gets a single Asset by its ID.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": Asset
   * }
   * }
   * <br><br>
   * <b>  PERMISSIONS  </b><br>
   * The user must have permissions for this Asset. See {@link AccessUtils} for definitions
   * concerning user access.
   */
  @RequestMapping(value = "/byAssetId/{assetId}", method = RequestMethod.GET)
  public AssetApiResponse getAssetByAssetId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID assetId) {
    return new AssetByAssetIdCommand()
        .setLocationRepository(locationRepository)
        .setDeviceRepository(deviceRepository)
        .setAssetRepository(assetRepository)
        .setUser(authenticatedUser)
        .setAssetId(assetId)
        .execute();
  }

  /**
   * <b>  GET /api/secure/assets/currentlyInDeviceId/{deviceId}  </b>
   * <br><br>
   * Gets the list of Assets currently in the Device with id "deviceId".
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "assets": [Asset]
   *   }
   * }
   * }
   * <br><br>
   * <b>  PERMISSIONS  </b><br>
   * The user must have permissions for the Device with id "deviceId". See {@link AccessUtils}
   * for definitions concerning user access.
   */
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

  /**
   * <b>  GET /api/secure/assets/currentlyInLocationId/{locationId}  </b>
   * <br><br>
   * Gets the list of Assets currently in the Location (aka "missing") with id "locationId".
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "assets": [Asset]
   *   }
   * }
   * }
   * <br><br>
   * <b>  PERMISSIONS  </b><br>
   * The user must have permission for the Location with id "locationId". See {@link AccessUtils}
   * for definitions concerning user access.
   */
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

  /**
   * <b>  GET /api/secure/assets/belongingToDeviceId/{deviceId}  </b>
   * <br><br>
   * Gets the list of Assets that belong to the Device with id "deviceId". That is to say, the
   * returned Assets all have their homeId set to "deviceId"
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "assets": [Asset]
   *   }
   * }
   * }
   * <br><br>
   * <b>  PERMISSIONS  </b><br>
   * The user must have permissions for the Device with id "deviceId". See {@link AccessUtils}
   * for definitions concerning user access.
   */
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

  /**
   * <b>  POST /api/secure/assets/forAuthenticated  </b>
   * <br><br>
   * This endpoint is to CREATE or UPDATE Assets.
   * <br><br>
   * To <b>CREATE</b> an Asset, supply the body in the following manner:
   * <br>
   * {@code
   * {
   *   (required) "homeId": ...,
   *   (required) "rfid": ...,
   *   (optional) "name": ...,
   *   (optional) "status": [AVAILABLE, REPAIR],
   *   (optional) "locationId": ...,
   *   (optional) "deviceId": ...
   * }
   * }
   * <br><br>
   * To <b>UPDATE</b> an Asset, supply the body in the following manner:
   * <br>
   * {@code
   * {
   *    (required) "id": ...,
   *    (optional) "name": ...,
   *    (optional) "status": ...,
   *    (optional) "homeId": ...,
   *    (optional) "locationId": ...,
   *    (optional) "deviceId": ...
   * }
   * }
   * <br><br>
   * <b>  RESULT:  </b><br>
   * {@code
   * {
   *   "status": 200,
   *   "message": "success",
   *   "response": Asset
   * }
   * }
   * <br><br>
   * <b>  PERMISSIONS  </b><br>
   * The user must have permission for the IDs specified for any of: {homeId, locationId, deviceId}.
   * See {@link AccessUtils} for definitions concerning user access
   */
  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.POST)
  public AssetApiResponse putAssetForAuthenticated(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody ApiAsset asset) {
    return new SaveAssetCommand()
        .setAssetRepository(assetRepository)
        .setDeviceRepository(deviceRepository)
        .setLocationRepository(locationRepository)
        .setUser(authenticatedUser)
        .setAsset(asset)
        .execute();
  }
}
