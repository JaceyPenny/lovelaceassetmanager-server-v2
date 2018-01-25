package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.notification.NotificationsForUserCommand;
import io.lovelacetech.server.command.notification.SaveNotificationCommand;
import io.lovelacetech.server.model.api.model.ApiNotification;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.notification.NotificationApiResponse;
import io.lovelacetech.server.model.api.response.notification.NotificationListApiResponse;
import io.lovelacetech.server.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/secure/notifications")
public class NotificationController extends BaseController {

  @Autowired
  NotificationRepository notificationRepository;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public NotificationListApiResponse getNotifications(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new NotificationListApiResponse()
        .setSuccess()
        .setResponse(notificationRepository.findAll());
  }

  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.GET)
  public NotificationListApiResponse getNotificationsForAuthenticated(
      @RequestAttribute ApiUser authenticatedUser) {
    return new NotificationsForUserCommand()
        .setNotificationRepository(notificationRepository)
        .setUser(authenticatedUser)
        .execute();
  }

  @RequestMapping(value = "/forAuthenticated", method = RequestMethod.POST)
  public NotificationApiResponse putNotificationForAuthenticated(
      @RequestAttribute ApiUser authenticatedUser,
      @RequestBody ApiNotification notification) {
    return new SaveNotificationCommand()
        .setNotificationRepository(notificationRepository)
        .setNotification(notification)
        .setUser(authenticatedUser)
        .execute();
  }
}
