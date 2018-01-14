package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.ArrayList;
import java.util.List;

public class ApiDeviceList extends BaseApiModel {
  private List<ApiDevice> devices;

  public ApiDeviceList() {
    this.devices = new ArrayList<>();
  }

  public ApiDeviceList(Iterable<Device> devices) {
    this.devices = RepositoryUtils.toApiList(devices);
  }

  public List<ApiDevice> getDevices() {
    return devices;
  }

  public ApiDeviceList setLocations(List<ApiDevice> devices) {
    this.devices = devices;
    return this;
  }

  public ApiDeviceList addLocation(ApiDevice device) {
    devices.add(device);
    return this;
  }
}