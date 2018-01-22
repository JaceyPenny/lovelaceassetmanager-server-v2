package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Notification;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.ArrayList;
import java.util.List;

public class ApiNotificationList extends BaseApiModel {
  private List<ApiNotification> notifications;

  public ApiNotificationList() {
    this.notifications = new ArrayList<>();
  }

  public ApiNotificationList(Iterable<Notification> notifications) {
    this.notifications = RepositoryUtils.toApiList(notifications);
  }

  public ApiNotificationList(List<ApiNotification> notifications) {
    this.notifications = notifications;
  }

  public List<ApiNotification> getNotifications() {
    return notifications;
  }

  public ApiNotificationList setNotifications(List<ApiNotification> notifications) {
    this.notifications = notifications;
    return this;
  }

  public ApiNotificationList addNotifications(ApiNotification notification) {
    notifications.add(notification);
    return this;
  }
}
