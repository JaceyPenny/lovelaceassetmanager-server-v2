package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.util.UUIDUtils;
import org.assertj.core.util.Strings;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

public class Device implements DatabaseModel<Device>, ApiModelConvertible<ApiDevice> {
  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "device_code", updatable = false, nullable = false)
  private String deviceCode;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "location_id", nullable = false)
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
