package io.lovelacetech.server.command.notification;

import io.lovelacetech.server.command.BaseCommand;
import io.lovelacetech.server.repository.NotificationRepository;

public abstract class NotificationCommand<T extends NotificationCommand> implements BaseCommand {
  private NotificationRepository notificationRepository;

  public NotificationRepository getNotificationRepository() {
    return notificationRepository;
  }

  public T setNotificationRepository(NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
    return (T) this;
  }

  @Override
  public boolean checkCommand() {
    return notificationRepository != null;
  }
}
