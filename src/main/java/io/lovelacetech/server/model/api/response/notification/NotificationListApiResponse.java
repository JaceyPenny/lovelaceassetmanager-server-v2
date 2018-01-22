package io.lovelacetech.server.model.api.response.notification;

import io.lovelacetech.server.model.Notification;
import io.lovelacetech.server.model.api.model.ApiNotificationList;
import io.lovelacetech.server.model.api.response.BaseApiResponse;

public class NotificationListApiResponse extends BaseApiResponse<NotificationListApiResponse, ApiNotificationList> {
  public NotificationListApiResponse setResponse(Iterable<Notification> devices) {
    return super.setResponse(new ApiNotificationList(devices));
  }
}
