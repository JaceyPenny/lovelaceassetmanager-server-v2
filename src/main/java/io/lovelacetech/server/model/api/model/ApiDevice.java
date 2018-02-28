package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.util.UUIDUtils;
import org.assertj.core.util.Strings;

import java.util.*;

public class ApiDevice extends BaseApiModel<Device> {
  private UUID id;
  private String deviceCode;
  private String name;
  private UUID locationId;

  private List<ApiAsset> assets;

  int assetsInDevice = 0;
  int assetsWithHome = 0;

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

  public int getAssetsInDevice() {
    return assetsInDevice;
  }

  public ApiDevice setAssetsInDevice(int assetsInDevice) {
    this.assetsInDevice = assetsInDevice;
    return this;
  }

  public int getAssetsWithHome() {
    return assetsWithHome;
  }

  public ApiDevice setAssetsWithHome(int assetsWithHome) {
    this.assetsWithHome = assetsWithHome;
    return this;
  }

  @Override
  public boolean isValid() {
    return !Strings.isNullOrEmpty(name)
        && !Strings.isNullOrEmpty(deviceCode);
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

  @Override
  public Map<String, Object> toLogObject() {
    Map<String, Object> resultingMap = new HashMap<>();

    resultingMap.put("deviceCode", deviceCode);
    resultingMap.put("name", name);
    resultingMap.put("locationId", locationId);

    return resultingMap;
  }

  public void sort() {
    assets.sort(Comparator.comparing(ApiAsset::getName));
  }
}
