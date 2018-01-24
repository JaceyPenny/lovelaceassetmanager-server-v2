package io.lovelacetech.server.model.api.model;

import java.util.UUID;

public class ApiDeviceActivation extends BaseApiModel {
  private String deviceCode;
  private UUID locationId;

  public ApiDeviceActivation setDeviceCode(String deviceCode) {
    this.deviceCode = deviceCode;
    return this;
  }

  public String getDeviceCode() {
    return deviceCode;
  }

  public ApiDeviceActivation setLocationId(UUID locationId) {
    this.locationId = locationId;
    return this;
  }

  public UUID getLocationId() {
    return locationId;
  }
}
