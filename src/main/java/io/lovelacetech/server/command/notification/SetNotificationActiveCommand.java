package io.lovelacetech.server.command.notification;

import io.lovelacetech.server.model.Notification;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.DefaultApiResponse;
import io.lovelacetech.server.repository.UserRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.UUID;

public class SetNotificationActiveCommand extends NotificationCommand<SetNotificationActiveCommand> {
  private ApiUser user;
  private boolean active;
  private UUID notificationId;
  private UserRepository userRepository;

  public SetNotificationActiveCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public SetNotificationActiveCommand setActive(boolean active) {
    this.active = active;
    return this;
  }

  public SetNotificationActiveCommand setNotificationId(UUID notificationId) {
    this.notificationId = notificationId;
    return this;
  }

  public SetNotificationActiveCommand setUserRepository(UserRepository userRepository) {
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
  public DefaultApiResponse execute() {
    if (!checkCommand()) {
      return new DefaultApiResponse().setDefault();
    }

    Notification updateNotification = getNotificationRepository().findOne(notificationId);
    if (updateNotification == null) {
      return new DefaultApiResponse().setNotFound();
    }

    if (!AccessUtils.userCanAccessNotification(user, updateNotification, userRepository)) {
      return new DefaultApiResponse().setAccessDenied();
    }

    updateNotification.setActive(active);
    getNotificationRepository().save(updateNotification);

    return new DefaultApiResponse().setSuccess();
  }
}
