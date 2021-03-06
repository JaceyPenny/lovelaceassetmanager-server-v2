package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.api.enums.AssetStatus;
import io.lovelacetech.server.util.UUIDUtils;
import org.assertj.core.util.Strings;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ApiAsset extends BaseApiModel<Asset> {
  private UUID id;
  private String name;
  private String rfid;
  private String serial;
  private AssetStatus status;
  private UUID homeId;
  private UUID locationId;
  private UUID deviceId;
  private ApiAssetType assetType;

  private ApiLog lastLog;

  public ApiAsset() {
    this.assetType = new ApiAssetType();
  }

  public ApiAsset(Asset asset) {
    this.id = asset.getId();
    this.name = asset.getName();
    this.rfid = asset.getRfid();
    this.serial = asset.getSerial();
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

  public String getSerial() {
    return serial;
  }

  public ApiAsset setSerial(String serial) {
    this.serial = serial;
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
        && serial != null
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
    asset.setSerial(serial);
    asset.setStatus(status);
    asset.setHomeId(homeId);
    asset.setLocationId(locationId);
    asset.setDeviceId(deviceId);
    asset.setAssetType(assetType.toDatabase());

    return asset;
  }

  @Override
  public Map<String, Object> toLogObject() {
    Map<String, Object> resultingMap = new HashMap<>();

    resultingMap.put("id", id);
    resultingMap.put("name", name);
    resultingMap.put("rfid", rfid);
    resultingMap.put("serial", serial);
    resultingMap.put("status", status);
    resultingMap.put("homeId", homeId);
    resultingMap.put("locationId", locationId);
    resultingMap.put("deviceId", deviceId);
    resultingMap.put("assetTypeId", assetType.getId());

    return resultingMap;
  }
}
