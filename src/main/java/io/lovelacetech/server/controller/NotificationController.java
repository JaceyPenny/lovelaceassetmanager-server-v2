package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.notification.NotificationsForUserCommand;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.notification.NotificationListApiResponse;
import io.lovelacetech.server.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
