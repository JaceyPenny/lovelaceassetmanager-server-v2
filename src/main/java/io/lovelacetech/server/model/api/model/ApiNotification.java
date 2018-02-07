package io.lovelacetech.server.model.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.lovelacetech.server.model.Notification;
import io.lovelacetech.server.model.api.enums.NotificationType;
import io.lovelacetech.server.model.api.serializer.ApiNotificationDeserializer;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.beans.Transient;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonDeserialize(using = ApiNotificationDeserializer.class)
public class ApiNotification extends BaseApiModel<Notification> {
  private UUID id;
  private NotificationType notificationType;
  private Time time;
  private UUID userId;
  private boolean active;

  private List<UUID> locationIds;
  private List<UUID> deviceIds;

  private List<ApiLocation> locations;
  private List<ApiDevice> devices;

  public ApiNotification() {
    this.id = UUIDUtils.empty();
    this.notificationType = NotificationType.EMAIL;
    this.time = new Time(0);
    this.userId = UUIDUtils.empty();
    this.active = false;

    locationIds = new ArrayList<>();
    deviceIds = new ArrayList<>();

    locations = new ArrayList<>();
    devices = new ArrayList<>();
  }

  public ApiNotification(Notification notification) {
    id = notification.getId();
    notificationType = notification.getNotificationType();
    time = notification.getTime();
    userId = notification.getUserId();
    active = notification.isActive();

    locationIds = new ArrayList<>();
    deviceIds = new ArrayList<>();
//    locationIds = RepositoryUtils.mapToIds(notification.getLocations());
//    deviceIds = RepositoryUtils.mapToIds(notification.getDevices());

    locations = RepositoryUtils.toApiList(notification.getLocations());
    devices = RepositoryUtils.toApiList(notification.getDevices());
  }

  public UUID getId() {
    return id;
  }

  public ApiNotification setId(UUID id) {
    this.id = id;
    return this;
  }

  public NotificationType getNotificationType() {
    return notificationType;
  }

  public ApiNotification setNotificationType(NotificationType notificationType) {
    this.notificationType = notificationType;
    return this;
  }

  public Time getTime() {
    return time;
  }

  public ApiNotification setTime(Time time) {
    this.time = time;
    return this;
  }

  public UUID getUserId() {
    return userId;
  }

  public ApiNotification setUserId(UUID userId) {
    this.userId = userId;
    return this;
  }

  public boolean isActive() {
    return active;
  }

  public ApiNotification setActive(boolean active) {
    this.active = active;
    return this;
  }

  @Transient
  public List<UUID> getLocationIds() {
    return locationIds;
  }

  public ApiNotification setLocationIds(List<UUID> locationIds) {
    this.locationIds = locationIds;
    return this;
  }

  public ApiNotification addLocationId(UUID locationId) {
    this.locationIds.add(locationId);
    return this;
  }

  @Transient
  public List<UUID> getDeviceIds() {
    return deviceIds;
  }

  public ApiNotification setDeviceIds(List<UUID> deviceIds) {
    this.deviceIds = deviceIds;
    return this;
  }

  public ApiNotification addDeviceId(UUID deviceId) {
    this.deviceIds.add(deviceId);
    return this;
  }

  public List<ApiLocation> getLocations() {
    return locations;
  }

  public ApiNotification setLocations(List<ApiLocation> locations) {
    this.locations = locations;
    return this;
  }

  public ApiNotification addLocation(ApiLocation location) {
    this.locations.add(location);
    return this;
  }

  public List<ApiDevice> getDevices() {
    return devices;
  }

  public ApiNotification setDevices(List<ApiDevice> devices) {
    this.devices = devices;
    return this;
  }

  public ApiNotification addDevice(ApiDevice device) {
    this.devices.add(device);
    return this;
  }

  @Override
  public boolean isValid() {
    System.out.println(this);
    return UUIDUtils.isValidId(userId)
        && time != null
        && notificationType != null
        && (!locations.isEmpty() || !devices.isEmpty());
  }

  @Override
  public Notification toDatabase() {
    Notification notification = new Notification();

    notification.setId(id);
    notification.setNotificationType(notificationType);
    notification.setTime(time);
    notification.setUserId(userId);
    notification.setActive(active);
    notification.setLocations(RepositoryUtils.toDatabaseList(locations));
    notification.setDevices(RepositoryUtils.toDatabaseList(devices));

    return notification;
  }
}
