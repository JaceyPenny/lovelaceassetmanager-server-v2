package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.enums.AssetStatus;
import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.converter.AssetStatusConverter;
import io.lovelacetech.server.util.UUIDUtils;
import org.h2.util.StringUtils;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "asset", schema = "lovelace")
public class Asset implements DatabaseModel<Asset>, ApiModelConvertible<ApiAsset> {
  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "name")
  private String name;

  @Column(name = "rfid", nullable = false, unique = true, updatable = false)
  private String rfid;

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
  @JoinTable(name = "asset_asset_type", schema = "lovelace",
    joinColumns = { @JoinColumn(name = "asset_id", nullable = false, updatable = false)},
    inverseJoinColumns = { @JoinColumn(name = "asset_type_id", nullable = false, updatable = false)})
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

    if (UUIDUtils.isValidId(other.homeId)) {
      homeId = other.homeId;
    }

    if (UUIDUtils.isValidId(other.locationId)) {
      locationId = other.locationId;
    }

    if (UUIDUtils.isValidId(other.deviceId)) {
      deviceId = other.deviceId;
    }

    if (other.assetType != null) {
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
