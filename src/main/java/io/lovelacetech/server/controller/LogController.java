package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.Log;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.log.LogListApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.repository.LogRepository;
import io.lovelacetech.server.util.AccessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/secure/logs")
public class LogController extends BaseController{
  private final LocationRepository locationRepository;
  private final DeviceRepository deviceRepository;
  private final AssetRepository assetRepository;
  private final LogRepository logRepository;

  @Autowired
  public LogController(
      LocationRepository locationRepository,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository,
      LogRepository logRepository) {
    this.locationRepository = locationRepository;
    this.deviceRepository = deviceRepository;
    this.assetRepository = assetRepository;
    this.logRepository = logRepository;
  }

  /**
   * <b>  GET /api/secure/logs/</b><br><br>
   * Gets all the Log objects in the database.
   * <br><br><b>  RESPONSE:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "logs": [Log]
   *   }
   * }}</pre>
   * <br><br><b>  PERMISSIONS:  </b><br>
   * This method is only accessible to SUPER users.
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public LogListApiResponse getLogs(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new LogListApiResponse()
        .setSuccess()
        .setResponse(logRepository.findAll());
  }

  /**
   * <b>  GET /api/secure/logs/byAssetId/{assetId}</b><br><br>
   * Gets all the Logs for the supplied assetId (UUID).
   * <br><br><b>  RESPONSE:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "logs": [Log]
   *   }
   * }}</pre>
   * <br><br><b>  PERMISSIONS:  </b><br>
   * The user must have access to the Asset they are requesting logs for.
   */
  @RequestMapping(value = "/byAssetId/{assetId}", method = RequestMethod.GET)
  public LogListApiResponse getLogsByAssetId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID assetId) {
    Asset asset = assetRepository.findOne(assetId);
    if (asset == null) {
      return new LogListApiResponse().setNotFound();
    }

    if (!AccessUtils.userCanAccessAsset(authenticatedUser, asset, deviceRepository, locationRepository)) {
      return new LogListApiResponse().setAccessDenied();
    }

    List<Log> logs = logRepository.findAllByObjectId(asset.getId());
    return new LogListApiResponse().setSuccess().setResponse(logs);
  }
}
