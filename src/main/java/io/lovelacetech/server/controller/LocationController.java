package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.location.*;
import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.location.LocationApiResponse;
import io.lovelacetech.server.model.api.response.location.LocationListApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/secure/locations")
public class LocationController extends BaseController {

  private final LocationRepository locationRepository;
  private final DeviceRepository deviceRepository;
  private final AssetRepository assetRepository;
  private final LogRepository logRepository;

  @Autowired
  public LocationController(
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
   * <b>  GET /api/secure/locations/ </b><br>
   * Gets all Locations in the database.<br><br>
   * <b>  RESPONSE:  </b>
   * <pre>{@code {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "locations": [Location]
   *   }
   * }}</pre>
   * <br><b>  PERMISSIONS:  </b>
   * <br>The user must be SUPER
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public LocationListApiResponse getLocations(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new LocationListApiResponse()
        .setSuccess()
        .setResponse(locationRepository.findAll());
  }

  /**
   * <b>  GET /api/secure/locations/byLocationId/{locationId}?filled="{true|false}  </b>
   * <br>Gets a location by the supplied "locationId". You can specify that you want the Location
   * filled with its children Devices and Assets by setting the request parameter "filled" to
   * "true".
   * <br><br><b>  RESPONSE:  </b>
   * <pre>{@code {
   *   "status": 200,
   *   "message": "success",
   *   "response": Location
   * }}</pre>
   * <br><b>  PERMISSIONS:  </b>
   * <br>The calling user must either:
   * <ul>
   *   <li>Be SUPER</li>
   *   <li>Be ADMIN of the Company possessing the Location with "locationId"</li>
   *   <li>Be USER that has been granted access to the Location with "locationId"</li>
   * </ul>
   */
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

  /**
   * <b>  GET /api/secure/locations/forAuthenticated?filled={true|false}</b>
   * <br>Gets all the Locations for the authenticated user's company, or the Locations the User
   * has been granted access to, if they are a USER. If you want each Location's children populated
   * in the response, specify "?filled=true" at the end of the URL.<br><br>
   * <b>  RESPONSE:  </b>
   * <pre>{@code {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "locations": [Location]
   *   }
   * }}</pre>
   */
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

  /**
   * <b>  GET /api/secure/locations/byCompanyId/{companyId}  </b>
   * <br> Gets all the locations in the Company identified by "companyId".
   * <br><br><b>  RESPONSE:  </b>
   * <pre>{@code {
   *   "status": 200,
   *   "message": "success",
   *   "response" {
   *     "locations": [Location]
   *   }
   * }}</pre>
   * <br><b>  PERMISSIONS:  </b><br>
   * The user must be SUPER.
   */
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

  /**
   * <b>  POST /api/secure/locations/forAuthenticated </b><br>
   * Adds a new location to the database for the authenticated user.
   * <br><br><b>  REQUEST BODY (CREATE):  </b>
   * <pre>{@code {
   *   (required) "name": String,
   *   (required) "city": String,
   *   (required) "state" String(2),  // e.g. "AR", "CA", ...
   * }}</pre>
   * <br><br>
   * <b>  REQUEST BODY (UPDATE):  </b>
   * <pre>{@code {
   *   (required) "id": UUID,
   *   (optional) "name": String
   *   (optional) "city": String,
   *   (optional) "state": String(2),
   * }}</pre><br><br>
   * <b>  RESPONSE:  </b>
   * <pre>{@code {
   *   "status": 200,
   *   "message": "success",
   *   "response": Location
   * }}</pre><br><br>
   * <b>  PERMISSIONS:  </b>
   * <br>The user must be an ADMIN at their company or a SUPER.
   */
  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.POST)
  public LocationApiResponse putLocationForAuthenticatedAdmin(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody ApiLocation location) {
    checkBelongsToCompany(authenticatedUser);

    return new SaveLocationCommand()
        .setLocationRepository(locationRepository)
        .setLogRepository(logRepository)
        .setLocation(location)
        .setUser(authenticatedUser)
        .execute();
  }

  /**
   * <b> DELETE /api/secure/locations/{locationId} </b><br>
   * Deletes a Location by id. The Location must be completely empty before total deletion.
   * <br><br>
   * <b>  REQUEST BODY:  </b>
   * <pre>{@code {
   *   "status": 200,
   *   "message": "success",
   *   "response": Location
   * }}</pre>
   * <br><b>  PERMISSIONS:  </b>
   * <br>The user must be an ADMIN for the company where they're trying to delete this location.
   */
  @RequestMapping(value = "/{locationId}", method = RequestMethod.DELETE)
  public LocationApiResponse deleteLocationById(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID locationId) {
    return new DeleteLocationCommand()
        .setLocationRepository(locationRepository)
        .setDeviceRepository(deviceRepository)
        .setAssetRepository(assetRepository)
        .setLogRepository(logRepository)
        .setLocationId(locationId)
        .setUser(authenticatedUser)
        .execute();
  }
}
