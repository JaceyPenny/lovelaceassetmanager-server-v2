package io.lovelacetech.server.command.notification;

import io.lovelacetech.server.model.Notification;
import io.lovelacetech.server.model.api.model.ApiNotification;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.notification.NotificationApiResponse;

public class SaveNotificationCommand extends NotificationCommand<SaveNotificationCommand> {
  private ApiUser user;
  private ApiNotification notification;

  public SaveNotificationCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public SaveNotificationCommand setNotification(ApiNotification notification) {
    this.notification = notification;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && notification != null;
  }

  @Override
  public NotificationApiResponse execute() {
    if (!checkCommand()) {
      return new NotificationApiResponse().setDefault();
    }

    notification.setUserId(user.getId());

    Notification notificationUpdate = notification.toDatabase();
    if (notificationUpdate.hasId()) {
      Notification existingNotification = getNotificationRepository()
          .findOne(notificationUpdate.getId());

      if (existingNotification == null) {
        return new NotificationApiResponse().setNotFound();
      }

      existingNotification.applyUpdate(notificationUpdate);
      notificationUpdate = existingNotification;
    } else {
      notification.setActive(true);
    }

    if (!notificationUpdate.toApi().isValid()) {
      return new NotificationApiResponse().setInvalidBody();
    }

    notificationUpdate = getNotificationRepository().save(notificationUpdate);

    return new NotificationApiResponse()
        .setSuccess()
        .setResponse(notificationUpdate.toApi());
  }
}
