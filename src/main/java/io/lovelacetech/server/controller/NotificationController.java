package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.notification.DeleteNotificationCommand;
import io.lovelacetech.server.command.notification.NotificationsForUserCommand;
import io.lovelacetech.server.command.notification.SaveNotificationCommand;
import io.lovelacetech.server.command.notification.SetNotificationActiveCommand;
import io.lovelacetech.server.model.api.model.ApiNotification;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.DefaultApiResponse;
import io.lovelacetech.server.model.api.response.notification.NotificationApiResponse;
import io.lovelacetech.server.model.api.response.notification.NotificationListApiResponse;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.repository.NotificationRepository;
import io.lovelacetech.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/secure/notifications")
public class NotificationController extends BaseController {

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final LocationRepository locationRepository;
  private final DeviceRepository deviceRepository;

  @Autowired
  public NotificationController(
      UserRepository userRepository,
      NotificationRepository notificationRepository,
      LocationRepository locationRepository,
      DeviceRepository deviceRepository) {
    this.userRepository = userRepository;
    this.notificationRepository = notificationRepository;
    this.locationRepository = locationRepository;
    this.deviceRepository = deviceRepository;
  }

  /**
   * <b>  GET /api/secure/notifications/</b><br><br>
   * Gets all the Notification objects in the database.
   * <br><br><b>  RESPONSE:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "notifications": [Notification]
   *   }
   * }}</pre>
   * <br><br><b>  PERMISSIONS:  </b><br>
   * This method is only accessible to SUPER users.
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public NotificationListApiResponse getNotifications(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new NotificationListApiResponse()
        .setSuccess()
        .setResponse(notificationRepository.findAll());
  }

  /**
   * <b>  GET /api/secure/notifications/forAuthenticated</b><br><br>
   * Gets all the Notification objects owned by the authenticated User.
   * <br><br><b>  RESPONSE:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "notifications": [Notification]
   *   }
   * }}</pre>
   * <br><br><b>  PERMISSIONS:  </b><br>
   * Must be an authenticated User.
   */
  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.GET)
  public NotificationListApiResponse getNotificationsForAuthenticated(
      @RequestAttribute ApiUser authenticatedUser) {
    checkBelongsToCompany(authenticatedUser);

    return new NotificationsForUserCommand()
        .setNotificationRepository(notificationRepository)
        .setUser(authenticatedUser)
        .execute();
  }

  /**
   * <b>  POST /api/secure/notifications/forAuthenticated  </b><br><br>
   * Creates a new Notification, or updates an existing notification.<br><br>
   * To <b>CREATE</b> a new notification, simply omit the "id" field from the following request
   * body. All new notifications must have at least one locationId or deviceId.
   * <br><br>
   * To <b>UPDATE</b> an existing notification, you must supply the "id" field in the following
   * request body. Updates on Notifications are only allowed to modify the "active" state of the
   * notification or the "notificationType". All other fields will be ignored for <b>UPDATE</b>.
   * <br><br>
   * <b>  REQUEST BODY (CREATE):  </b>
   * <pre>{@code    {
   *   (required) "notificationType": {EMAIL|TEXT|EMAIL_TEXT},
   *   (required) "time": "hh:mm:ss",
   *   (semi-optional) "locationIds": [locationId1, locationId2, ...],
   *   (semi-optional) "deviceIds": [deviceId1, deviceId2, ...]
   * }}</pre>
   * <br>
   * <b>  REQUEST BODY (UPDATE):  </b>
   * <pre>{@code    {
   *   (required) "id": Notification.id,
   *   (optional) "notificationType": {EMAIL|TEXT|EMAIL_TEXT}
   *   (optional) "time": "hh:mm:ss"
   * }}</pre>
   * <br><br>
   * <b>  RESPONSE:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": Notification
   * }}</pre>
   * <br><br><b>  PERMISSIONS:  </b><br>
   * Must be an authenticated User.
   */
  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.POST)
  public NotificationApiResponse putNotificationForAuthenticated(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody ApiNotification notification) {
    checkBelongsToCompany(authenticatedUser);

    return new SaveNotificationCommand()
        .setNotificationRepository(notificationRepository)
        .setLocationRepository(locationRepository)
        .setDeviceRepository(deviceRepository)
        .setNotification(notification)
        .setUser(authenticatedUser)
        .execute();
  }

  /**
   * <b>  PUT /api/secure/notifications/{notificationId}?active={true|false}</b><br>
   * Sets the state of a notification to "active" or "inactive" depending on the supplied
   * parameter "active" at the end of the request URL.<br><br>
   * <b>  RESPONSE:  </b>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": null
   * }}</pre>
   * <br><b>  PERMISSIONS:  </b>
   * <br>The user must either:
   * <ul>
   *   <li>Own the notification</li>
   *   <li>Be an ADMIN and own the owner of the notification</li>
   *   <li>Be a SUPER</li>
   * </ul>
   */
  @RequestMapping(value = "/{notificationId}", method = RequestMethod.PUT)
  public DefaultApiResponse setNotificationActive(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID notificationId,
      @RequestParam boolean active) {
    return new SetNotificationActiveCommand()
        .setNotificationRepository(notificationRepository)
        .setUser(authenticatedUser)
        .setActive(active)
        .setNotificationId(notificationId)
        .setUserRepository(userRepository)
        .execute();
  }

  /**
   * <b>  DELETE /api/secure/notifications/{notificationId}</b><br>
   * Deletes a notification by id. Returns the deleted notification in the response body.
   * <br><br><b>  RESPONSE:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": Notification
   * }}</pre>
   * <br><b>  PERMISSIONS:  </b><br>
   * The authenticated user must either:
   * <ul>
   *   <li>Own the notification</li>
   *   <li>Be an ADMIN and own the owner of the notification</li>
   *   <li>Be a SUPER</li>
   * </ul>
   */
  @RequestMapping(value = "/{notificationId}", method = RequestMethod.DELETE)
  public NotificationApiResponse deleteNotificationById(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable UUID notificationId) {
    return new DeleteNotificationCommand()
        .setUserRepository(userRepository)
        .setNotificationRepository(notificationRepository)
        .setNotificationId(notificationId)
        .setUser(authenticatedUser)
        .execute();
  }
}
