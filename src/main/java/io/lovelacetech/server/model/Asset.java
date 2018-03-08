package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.enums.AssetStatus;
import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.converter.AssetStatusConverter;
import io.lovelacetech.server.util.UUIDUtils;
import org.h2.util.StringUtils;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "asset", schema = "lovelace")
public class Asset implements DatabaseModel<Asset>, ApiModelConvertible<ApiAsset> {
  @Id
  @GeneratedValue
  @ColumnDefault("uuid_generate_v4()")
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "name")
  private String name;

  @Column(name = "rfid", nullable = false, unique = true, updatable = false)
  private String rfid;

  @Column(name = "serial", nullable = false, unique = true, updatable = true)
  private String serial;

  @Convert(converter = AssetStatusConverter.class)
  @Column(name = "status")
  private AssetStatus status;

  @Column(name = "home_id", nullable = false)
  private UUID homeId;

  @Column(name = "location_id")
  private UUID locationId;

  @Column(name = "device_id")
  private UUID deviceId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "asset_type_id")
  private AssetType assetType;

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

    if (other.status != null) {
      status = other.status;
    }

    if (other.serial != null) {
      serial = other.serial;
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

    if (other.assetType != null && !other.assetType.toApi().isEmpty()) {
      assetType = other.assetType;
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

  public String getSerial() {
    return serial;
  }

  public void setSerial(String serial) {
    this.serial = serial;
  }

  public AssetStatus getStatus() {
    return status;
  }

  public void setStatus(AssetStatus status) {
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

  public AssetType getAssetType() {
    return assetType;
  }

  public void setAssetType(AssetType assetType) {
    this.assetType = assetType;
  }
}
