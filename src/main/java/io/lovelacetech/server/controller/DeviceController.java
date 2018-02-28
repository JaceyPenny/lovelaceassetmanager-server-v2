package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.device.*;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.model.ApiDeviceActivation;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.device.DeviceApiResponse;
import io.lovelacetech.server.model.api.response.device.DeviceListApiResponse;
import io.lovelacetech.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/secure/devices")
public class DeviceController extends BaseController {

  private final CompanyRepository companyRepository;
  private final LocationRepository locationRepository;
  private final DeviceRepository deviceRepository;
  private final AssetRepository assetRepository;
  private final LogRepository logRepository;

  @Autowired
  DeviceController(
      CompanyRepository companyRepository,
      LocationRepository locationRepository,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository,
      LogRepository logRepository) {
    this.companyRepository = companyRepository;
    this.locationRepository = locationRepository;
    this.deviceRepository = deviceRepository;
    this.assetRepository = assetRepository;
    this.logRepository = logRepository;
  }

  /**
   * <b> GET /api/secure/devices/ </b>
   * <br> Gets all the devices in the database.
   * <br><br><b>  RESPONSE:  </b>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "devices": [Device]
   *   }
   * }}</pre><br><br>
   * <b> PERMISSIONS: </b><br>
   * The user must be a SUPER.
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public DeviceListApiResponse getDevices(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new DeviceListApiResponse()
        .setSuccess()
        .setResponse(deviceRepository.findAll());
  }

  /**
   * <b> GET /api/secure/devices/byDeviceId/{deviceId}?filled={true|false} </b>
   * <br>Gets a single Device by "deviceId".
   * <br><br><b>  RESPONSE:  </b>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": Device
   * }}</pre><br><br>
   * <b>  PERMISSIONS:  </b>
   * The user must have permissions for this Device. They must either:
   * <ul>
   *   <li>Be a SUPER</li>
   *   <li>Be an ADMIN of the company that this Device belongs to</li>
   *   <li>Be a USER with permissions for the Location this Device belongs to.</li>
   * </ul>
   */
  @RequestMapping(value = "/byDeviceId/{deviceId}", method = RequestMethod.GET)
  public DeviceApiResponse getDeviceByDeviceId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID deviceId,
      @RequestParam(defaultValue = "false") boolean filled) {
    checkBelongsToCompany(authenticatedUser);

    return new DeviceByDeviceIdCommand()
        .setDeviceRepository(deviceRepository)
        .setLocationRepository(locationRepository)
        .setAssetRepository(assetRepository)
        .setDeviceId(deviceId)
        .setUser(authenticatedUser)
        .setFilled(filled)
        .execute();
  }

  /**
   * <b> GET /api/secure/devices/byLocationId/{locationId}?filled={true|false} </b><br>
   * Gets the Devices at the Location with "locationId".<br><br>
   * <b>  RESPONSE:  </b>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "devices": [Device]
   *   }
   * }}</pre><br><br>
   * <b>  PERMISSIONS:  </b><br>
   * The user must have permissions for the Location with "locationId". The user must either:
   * <ul>
   *   <li>Be a SUPER</li>
   *   <li>Be an ADMIN of the company that the Location belongs to</li>
   *   <li>Be a user with permissions for the Location with "locationId"</li>
   * </ul>
   */
  @RequestMapping(value = "/byLocationId/{locationId}", method = RequestMethod.GET)
  public DeviceListApiResponse getDevicesByLocationId(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID locationId,
      @RequestParam(defaultValue = "true") boolean filled) {
    checkBelongsToCompany(authenticatedUser);

    return new DeviceByLocationIdCommand()
        .setDeviceRepository(deviceRepository)
        .setLocationId(locationId)
        .setUser(authenticatedUser)
        .setFilled(filled)
        .setAssetRepository(assetRepository)
        .setLocationRepository(locationRepository)
        .execute();
  }

  /**
   * <b> GET /api/secure/devices/forAuthenticated?filled={true|false}</b>
   * <br>Gets all the devices belonging to the authenticated user. Specify the "filled" parameter
   * to have the device populated with its children Assets.
   * <br><br><b>  RESPONSE:  </b>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "devices": [Device]
   *   }
   * }}</pre><br><br>
   * <b>  PERMISSIONS:  </b>
   * User must be authenticated.
   */
  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.GET)
  public DeviceListApiResponse getDevicesForAuthenticated(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestParam(defaultValue = "false") boolean filled) {
    return new DevicesForUserCommand()
        .setDeviceRepository(deviceRepository)
        .setLocationRepository(locationRepository)
        .setCompanyRepository(companyRepository)
        .setFilled(filled)
        .setUser(authenticatedUser)
        .execute();
  }

  /**
   * <b> POST /api/secure/devices/forAuthenticated </b>
   * <br>Creates or updates a Device in the database.<br><br>
   * <b>  REQUEST BODY (CREATE):</b>
   * <pre>{@code    {
   *   (required) "deviceCode": String,
   *   (required) "name": String,
   *   (optional) "locationId": UUID
   * }}</pre>
   * This method should really only be used by Lovelace employees to register
   * a manufactured device.
   * <br><br><b>  REQUEST BODY (UPDATE):  </b>
   * <pre>{@code    {
   *   (required) "id": UUID,
   *   (optional) "name": String,
   *   (optional) "locationId": UUID
   * }}</pre>
   * <br><br>
   * <b>  RESPONSE:  </b>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": Device
   * }}</pre><br>
   * <b>  PERMISSIONS:  </b>
   * To create, user must be SUPER. To update, user must have permissions
   * on the Location for this Device.
   */
  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.POST)
  public DeviceApiResponse putDeviceForAuthenticated(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody ApiDevice device) {
    checkBelongsToCompany(authenticatedUser);

    return new SaveDeviceCommand()
        .setLocationRepository(locationRepository)
        .setDeviceRepository(deviceRepository)
        .setAssetRepository(assetRepository)
        .setLogRepository(logRepository)
        .setUser(authenticatedUser)
        .setDevice(device)
        .execute();
  }

  /**
   * <b> POST /api/secure/devices/activateDevice </b><br>
   * Activates a new Device by device code, adding it to the location identified
   * by "locationId".<br><br>
   * <b>  REQUEST BODY:  </b>
   * <pre>{@code    {
   *   (required) "deviceCode": String,
   *   (required) "locationId": UUID
   * }}</pre><br><br>
   * <b>  RESPONSE:  </b>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": Device
   * }}</pre>
   * <br><b>  PERMISSIONS:  </b>
   * <br>The user must be an ADMIN for the Company where they're trying to activate
   * this new Device.
   */
  @RequestMapping(value = "/activateDevice", method = RequestMethod.POST)
  public DeviceApiResponse activateDeviceWithCode(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody ApiDeviceActivation deviceActivation) {
    checkBelongsToCompany(authenticatedUser);

    return new ActivateDeviceCommand()
        .setLocationRepository(locationRepository)
        .setDeviceRepository(deviceRepository)
        .setAssetRepository(assetRepository)
        .setLogRepository(logRepository)
        .setUser(authenticatedUser)
        .setDeviceActivation(deviceActivation)
        .execute();
  }

  /**
   * <b> DELETE /api/secure/devices/{deviceId} </b><br>
   * Deletes a Device by id. In actuality, the device is not removed from the database. Instead,
   * its locationId is set to null, effectively "deactivating" it from the user's point of view.
   * <br><br>
   * <b>  REQUEST BODY:  </b>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": Device
   * }}</pre>
   * <br><b>  PERMISSIONS:  </b>
   * <br>The user must be an ADMIN for the company where they're trying to delete this device.
   */
  @RequestMapping(value = "/{deviceId}", method = RequestMethod.DELETE)
  public DeviceApiResponse deleteDeviceById(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID deviceId) {
    return new DeleteDeviceCommand()
        .setUser(authenticatedUser)
        .setDeviceId(deviceId)
        .setAssetRepository(assetRepository)
        .setDeviceRepository(deviceRepository)
        .setLocationRepository(locationRepository)
        .setLogRepository(logRepository)
        .execute();
  }
}
