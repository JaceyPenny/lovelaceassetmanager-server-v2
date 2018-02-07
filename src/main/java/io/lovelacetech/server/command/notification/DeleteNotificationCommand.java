package io.lovelacetech.server.command.notification;

import io.lovelacetech.server.model.Notification;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.notification.NotificationApiResponse;
import io.lovelacetech.server.repository.UserRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.UUID;

public class DeleteNotificationCommand extends NotificationCommand<DeleteNotificationCommand> {
  private ApiUser user;
  private UUID notificationId;

  private UserRepository userRepository;

  public DeleteNotificationCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public DeleteNotificationCommand setNotificationId(UUID notificationId) {
    this.notificationId = notificationId;
    return this;
  }

  public DeleteNotificationCommand setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && UUIDUtils.isValidId(notificationId)
        && userRepository != null;
  }

  @Override
  public NotificationApiResponse execute() {
    if (!checkCommand()) {
      return new NotificationApiResponse().setDefault();
    }

    Notification deletedNotification = getNotificationRepository().findOne(notificationId);
    if (deletedNotification == null) {
      return new NotificationApiResponse().setNotFound();
    }

    if (!AccessUtils.userCanAccessNotification(user, deletedNotification, userRepository)) {
      return new NotificationApiResponse().setAccessDenied();
    }

    getNotificationRepository().delete(deletedNotification);
    return new NotificationApiResponse()
        .setSuccess()
        .setResponse(deletedNotification.toApi());
  }
}
