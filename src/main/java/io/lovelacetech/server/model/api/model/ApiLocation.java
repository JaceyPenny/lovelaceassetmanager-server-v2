package io.lovelacetech.server.model.api.model;

import com.google.common.base.Strings;
import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApiLocation extends BaseApiModel<Location> {
  private UUID id;
  private String name;
  private String city;
  private String state;
  private UUID companyId;

  private List<ApiDevice> devices;
  private List<ApiAsset> assets;

  public ApiLocation() {
    this.id = UUIDUtils.empty();
    this.name = "";
    this.city = "";
    this.state = "";
    this.companyId = UUIDUtils.empty();
    this.devices = new ArrayList<>();
    this.assets = new ArrayList<>();
  }

  public ApiLocation(Location location) {
    this.id = location.getId();
    this.name = location.getName();
    this.city = location.getCity();
    this.state = location.getState();
    this.companyId = location.getCompanyId();
    this.devices = new ArrayList<>();
    this.assets = new ArrayList<>();
  }

  public UUID getId() {
    return id;
  }

  public ApiLocation setId(UUID id) {
    this.id = id;
    return this;
  }

  public ApiLocation setName(String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
  }

  public String getCity() {
    return city;
  }

  public ApiLocation setCity(String city) {
    this.city = city;
    return this;
  }

  public String getState() {
    return state;
  }

  public ApiLocation setState(String state) {
    this.state = state;
    return this;
  }

  public UUID getCompanyId() {
    return companyId;
  }

  public ApiLocation setCompanyId(UUID companyId) {
    this.companyId = companyId;
    return this;
  }

  public List<ApiDevice> getDevices() {
    return devices;
  }

  public ApiLocation setDevices(List<ApiDevice> devices) {
    this.devices = devices;
    return this;
  }

  public ApiLocation addDevice(ApiDevice device) {
    devices.add(device);
    return this;
  }

  public List<ApiAsset> getAssets() {
    return assets;
  }

  public ApiLocation setAssets(List<ApiAsset> assets) {
    this.assets = assets;
    return this;
  }

  public ApiLocation addAsset(ApiAsset asset) {
    assets.add(asset);
    return this;
  }

  @Override
  public boolean isValid() {
    return !Strings.isNullOrEmpty(name)
        && !Strings.isNullOrEmpty(city)
        && !Strings.isNullOrEmpty(state)
        && UUIDUtils.isValidId(companyId);
  }

  @Override
  public Location toDatabase() {
    Location location = new Location();

    location.setId(UUIDUtils.isValidId(id) ? id : null);
    location.setName(name);
    location.setCity(city);
    location.setState(state);
    location.setCompanyId(UUIDUtils.isValidId(companyId) ? companyId : null);

    return location;
  }
}
