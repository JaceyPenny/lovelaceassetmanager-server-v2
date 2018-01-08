package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.util.UUIDUtils;
import org.h2.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

public class Asset implements DatabaseModel<Asset>, ApiModelConvertible<ApiAsset> {
  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "name")
  private String name;

  @Column(name = "rfid", nullable = false, unique = true, updatable = false)
  private String rfid;

  @Column(name = "status")
  private String status;

  @Column(name = "home_id", nullable = false)
  private UUID homeId;

  @Column(name = "location_id")
  private UUID locationId;

  @Column(name = "device_id")
  private UUID deviceId;

  @Override
  public ApiAsset toApi() {
    return new ApiAsset(this);
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
  public void applyUpdate(Asset other) {
    if (!StringUtils.isNullOrEmpty(other.name)) {
      name = other.name;
    }

    if (!StringUtils.isNullOrEmpty(other.status)) {
      status = other.status;
    }

    if (UUIDUtils.isValidId(other.homeId)) {
      homeId = other.homeId;
    }

    if (UUIDUtils.isValidId(other.locationId)) {
      locationId = other.locationId;
    }

    if (UUIDUtils.isValidId(other.deviceId)) {
      deviceId = other.deviceId;
    }
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRfid() {
    return rfid;
  }

  public void setRfid(String rfid) {
    this.rfid = rfid;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public UUID getHomeId() {
    return homeId;
  }

  public void setHomeId(UUID homeId) {
    this.homeId = homeId;
  }

  public UUID getLocationId() {
    return locationId;
  }

  public void setLocationId(UUID locationId) {
    this.locationId = locationId;
  }

  public UUID getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(UUID deviceId) {
    this.deviceId = deviceId;
  }
}
