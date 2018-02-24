package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.api.enums.AssetStatus;
import io.lovelacetech.server.util.UUIDUtils;
import org.assertj.core.util.Strings;

import java.beans.Transient;
import java.util.UUID;

public class ApiAsset extends BaseApiModel<Asset> {
  private UUID id;
  private String name;
  private String rfid;
  private AssetStatus status;
  private UUID homeId;
  private UUID locationId;
  private UUID deviceId;
  private ApiAssetType assetType;

  private ApiLog lastLog;

  public ApiAsset() {
    this.id = null;
    this.name = "";
    this.rfid = "";
    this.status = AssetStatus.AVAILABLE;
    this.homeId = null;
    this.locationId = null;
    this.deviceId = null;
    this.assetType = new ApiAssetType();
  }

  public ApiAsset(Asset asset) {
    this.id = asset.getId();
    this.name = asset.getName();
    this.rfid = asset.getRfid();
    this.status = asset.getStatus();
    this.homeId = asset.getHomeId();
    this.locationId = asset.getLocationId();
    this.deviceId = asset.getDeviceId();
    this.assetType = new ApiAssetType(asset.getAssetType());
  }

  public UUID getId() {
    return id;
  }

  public ApiAsset setId(UUID id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public ApiAsset setName(String name) {
    this.name = name;
    return this;
  }

  public String getRfid() {
    return rfid;
  }

  public ApiAsset setRfid(String rfid) {
    this.rfid = rfid;
    return this;
  }

  public AssetStatus getStatus() {
    return status;
  }

  public ApiAsset setStatus(AssetStatus status) {
    this.status = status;
    return this;
  }

  public ApiAsset setStatus(String status) {
    setStatus(AssetStatus.fromString(status));
    return this;
  }

  public UUID getHomeId() {
    return homeId;
  }

  public ApiAsset setHomeId(UUID homeId) {
    this.homeId = homeId;
    return this;
  }

  public UUID getLocationId() {
    return locationId;
  }

  public ApiAsset setLocationId(UUID locationId) {
    this.locationId = locationId;
    return this;
  }

  public UUID getDeviceId() {
    return deviceId;
  }

  public ApiAsset setDeviceId(UUID deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  @Transient
  public ApiAssetType getAssetType() {
    return assetType;
  }

  @Transient
  public ApiAsset setAssetType(ApiAssetType assetType) {
    this.assetType = assetType;
    return this;
  }

  public String getType() {
    return (assetType == null) ? null : assetType.getType();
  }

  public void setType(String type) {
    assetType = new ApiAssetType();
    assetType.setType(type);
  }

  public ApiLog getLastLog() {
    return lastLog;
  }

  public ApiAsset setLastLog(ApiLog lastLog) {
    this.lastLog = lastLog;
    return this;
  }

  public String getLastActivityString() {
    if (lastLog == null) {
      return "(unknown)";
    }

    return lastLog.getFormattedTime();
  }

  @Override
  public boolean isValid() {
    return !Strings.isNullOrEmpty(name)
        && !Strings.isNullOrEmpty(rfid)
        && UUIDUtils.isValidId(homeId)
        && assetType != null
        && status != null;
  }

  @Override
  public Asset toDatabase() {
    Asset asset = new Asset();

    asset.setId(UUIDUtils.isValidId(id) ? id : null);
    asset.setName(name);
    asset.setRfid(rfid);
    asset.setStatus(status);
    asset.setHomeId(homeId);
    asset.setLocationId(locationId);
    asset.setDeviceId(deviceId);
    asset.setAssetType(assetType.toDatabase());

    return asset;
  }
}
