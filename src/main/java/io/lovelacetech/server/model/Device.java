package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.util.UUIDUtils;
import org.assertj.core.util.Strings;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "device", schema = "lovelace")
public class Device implements DatabaseModel<Device>, ApiModelConvertible<ApiDevice> {
  @Id
  @GeneratedValue
  @ColumnDefault("uuid_generate_v4()")
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "device_code", updatable = false, nullable = false)
  private String deviceCode;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "location_id")
  private UUID locationId;

  @Override
  public ApiDevice toApi() {
    return new ApiDevice(this);
  }

  @Override
  public boolean hasId() {
    return UUIDUtils.isValidId(id);
  }

  @Override
  public boolean idEquals(UUID otherId) {
    return UUIDUtils.idsEqual(id, otherId);
  }

  @Override
  public void applyUpdate(Device other) {
    if (!Strings.isNullOrEmpty(other.name)) {
      name = other.name;
    }

    if (UUIDUtils.isValidId(other.locationId)) {
      locationId = other.locationId;
    }
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getDeviceCode() {
    return deviceCode;
  }

  public void setDeviceCode(String deviceCode) {
    this.deviceCode = deviceCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UUID getLocationId() {
    return locationId;
  }

  public void setLocationId(UUID locationId) {
    this.locationId = locationId;
  }
}
