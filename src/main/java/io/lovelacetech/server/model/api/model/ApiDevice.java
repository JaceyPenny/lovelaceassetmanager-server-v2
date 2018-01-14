package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApiDevice extends BaseApiModel {
  private UUID id;
  private String deviceCode;
  private String name;
  private UUID locationId;

  private List<ApiAsset> assets;

  public ApiDevice() {
    this.id = UUIDUtils.empty();
    this.deviceCode = "";
    this.name = "";
    this.locationId = UUIDUtils.empty();
    this.assets = new ArrayList<>();
  }

  public ApiDevice(Device device) {
    this.id = device.getId();
    this.deviceCode = device.getDeviceCode();
    this.name = device.getName();
    this.locationId = device.getLocationId();
    this.assets = new ArrayList<>();
  }


  public UUID getId() {
    return id;
  }

  public ApiDevice setId(UUID id) {
    this.id = id;
    return this;
  }

  public String getDeviceCode() {
    return deviceCode;
  }

  public ApiDevice setDeviceCode(String deviceCode) {
    this.deviceCode = deviceCode;
    return this;
  }

  public String getName() {
    return name;
  }

  public ApiDevice setName(String name) {
    this.name = name;
    return this;
  }

  public UUID getLocationId() {
    return locationId;
  }

  public ApiDevice setLocationId(UUID locationId) {
    this.locationId = locationId;
    return this;
  }

  public List<ApiAsset> getAssets() {
    return assets;
  }

  public ApiDevice setAssets(List<ApiAsset> assets) {
    this.assets = assets;
    return this;
  }

  public ApiDevice addAsset(ApiAsset asset) {
    this.assets.add(asset);
    return this;
  }

  @Override
  public Device toDatabase() {
    Device device = new Device();

    device.setId(UUIDUtils.isValidId(id) ? id : null);
    device.setDeviceCode(deviceCode);
    device.setName(name);
    device.setLocationId(UUIDUtils.isValidId(locationId) ? locationId : null);

    return device;
  }
}
