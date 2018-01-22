package io.lovelacetech.server.command.notification;

import io.lovelacetech.server.model.api.model.ApiNotification;
import io.lovelacetech.server.model.api.model.ApiNotificationList;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.notification.NotificationListApiResponse;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.List;

public class NotificationsForUserCommand extends NotificationCommand<NotificationsForUserCommand> {
  private ApiUser user;

  public NotificationsForUserCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand() && user != null;
  }

  @Override
  public NotificationListApiResponse execute() {
    if (!checkCommand()) {
      return new NotificationListApiResponse().setDefault();
    }

    List<ApiNotification> notifications = RepositoryUtils.toApiList(
        getNotificationRepository().findAllByUserId(user.getId()));

    return new NotificationListApiResponse()
        .setSuccess()
        .setResponse(new ApiNotificationList(notifications));
  }
}
