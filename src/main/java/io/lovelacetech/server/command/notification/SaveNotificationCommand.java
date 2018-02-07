package io.lovelacetech.server.command.notification;

import com.google.common.collect.Lists;
import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.Notification;
import io.lovelacetech.server.model.api.model.ApiNotification;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.notification.NotificationApiResponse;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.List;

public class SaveNotificationCommand extends NotificationCommand<SaveNotificationCommand> {
  private ApiUser user;
  private ApiNotification notification;
  private LocationRepository locationRepository;
  private DeviceRepository deviceRepository;

  public SaveNotificationCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public SaveNotificationCommand setNotification(ApiNotification notification) {
    this.notification = notification;
    return this;
  }

  public SaveNotificationCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  public SaveNotificationCommand setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && notification != null
        && locationRepository != null
        && deviceRepository != null;
  }

  @Override
  public NotificationApiResponse execute() {
    if (!checkCommand()) {
      return new NotificationApiResponse().setDefault();
    }

    notification.setUserId(user.getId());

    Notification notificationUpdate = notification.toDatabase();
    if (notificationUpdate.hasId()) {
      // If the notification exists already, we will only allow the user to update the "active"
      // state or the notification type.
      Notification existingNotification = getNotificationRepository()
          .findOne(notificationUpdate.getId());

      if (existingNotification == null) {
        return new NotificationApiResponse().setNotFound();
      }

      if (!UUIDUtils.idsEqual(existingNotification.getUserId(), user.getId())) {
        return new NotificationApiResponse().setAccessDenied();
      }

      existingNotification.applyUpdate(notificationUpdate);
      notificationUpdate = existingNotification;

      notificationUpdate = getNotificationRepository().save(notificationUpdate);
      return new NotificationApiResponse()
          .setSuccess()
          .setResponse(notificationUpdate.toApi());

    } else {
      notificationUpdate.setActive(true);
    }

    // At this point, we are guaranteed that the user is creating a brand new notification
    if (!notification.getLocationIds().isEmpty()) {
      List<Location> locations = Lists.newArrayList(
          locationRepository.findAll(notification.getLocationIds()));
      if (!locations.isEmpty()) {
        if (!AccessUtils.userCanAccessLocations(user, locations)) {
          return new NotificationApiResponse().setAccessDenied();
        }

        notificationUpdate.setLocations(locations);
      }
    }

    if (!notification.getDeviceIds().isEmpty()) {
      List<Device> devices = Lists.newArrayList(
          deviceRepository.findAll(notification.getDeviceIds()));
      if (!devices.isEmpty()) {
        if (!AccessUtils.userCanAccessDevices(user, devices, locationRepository)) {
          return new NotificationApiResponse().setAccessDenied();
        }

        notificationUpdate.setDevices(devices);
      }
    }

    System.out.println(notificationUpdate.getNotificationType());
    if (!notificationUpdate.toApi().isValid()) {
      return new NotificationApiResponse().setInvalidBody();
    }

    notificationUpdate = getNotificationRepository().save(notificationUpdate);

    return new NotificationApiResponse()
        .setSuccess()
        .setResponse(notificationUpdate.toApi());
  }
}
