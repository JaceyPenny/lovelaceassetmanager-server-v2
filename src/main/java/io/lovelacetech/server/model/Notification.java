package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.enums.NotificationType;
import io.lovelacetech.server.model.api.model.ApiNotification;
import io.lovelacetech.server.model.converter.NotificationTypeConverter;
import io.lovelacetech.server.util.UUIDUtils;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "notification", schema = "lovelace")
public class Notification implements DatabaseModel<Notification>, ApiModelConvertible<ApiNotification> {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Convert(converter = NotificationTypeConverter.class)
  @Column(name = "notification_type", nullable = false)
  private NotificationType notificationType;

  @Column(name = "time", nullable = false)
  private Time time;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Column(name = "active", nullable = false)
  private Boolean active;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "notification_items", schema = "lovelace",
      joinColumns = {@JoinColumn(name = "notification_id", nullable = false, updatable = false)},
      inverseJoinColumns = {@JoinColumn(name = "location_id", nullable = false, updatable = false)})
  private List<Location> locations;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "notification_items", schema = "lovelace",
      joinColumns = {@JoinColumn(name = "notification_id", nullable = false, updatable = false)},
      inverseJoinColumns = {@JoinColumn(name = "device_id", nullable = false, updatable = false)})
  private List<Device> devices;

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  @Transient
  public boolean hasId() {
    return UUIDUtils.isValidId(id);
  }

  @Override
  @Transient
  public boolean idEquals(UUID otherId) {
    return UUIDUtils.idsEqual(id, otherId);
  }

  @Override
  @Transient
  public void applyUpdate(Notification other) {
    if (other.notificationType != null) {
      notificationType = other.notificationType;
    }

    if (other.active != null) {
      active = other.active;
    }
  }

  @Override
  @Transient
  public ApiNotification toApi() {
    return new ApiNotification(this);
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public NotificationType getNotificationType() {
    return notificationType;
  }

  public void setNotificationType(NotificationType notificationType) {
    this.notificationType = notificationType;
  }

  public Time getTime() {
    return time;
  }

  public void setTime(Time time) {
    this.time = time;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Transient
  public List<Location> getLocations() {
    return locations;
  }

  @Transient
  public void setLocations(List<Location> locations) {
    this.locations = locations;
  }

  @Transient
  public List<Device> getDevices() {
    return devices;
  }

  @Transient
  public void setDevices(List<Device> devices) {
    this.devices = devices;
  }
}
