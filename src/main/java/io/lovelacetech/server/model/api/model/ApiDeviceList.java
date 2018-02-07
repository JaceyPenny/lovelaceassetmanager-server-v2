package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ApiDeviceList extends BaseApiModel {
  private List<ApiDevice> devices;

  public ApiDeviceList() {
    this.devices = new ArrayList<>();
  }

  public ApiDeviceList(Iterable<Device> devices) {
    this.devices = RepositoryUtils.toApiList(devices);
  }

  public ApiDeviceList(List<ApiDevice> devices) {
    this.devices = devices;
  }

  public List<ApiDevice> getDevices() {
    sort();
    return devices;
  }

  public ApiDeviceList setDevices(List<ApiDevice> devices) {
    this.devices = devices;
    return this;
  }

  public ApiDeviceList addDevice(ApiDevice device) {
    devices.add(device);
    return this;
  }

  public void sort() {
    devices.forEach(ApiDevice::sort);
    devices.sort(Comparator.comparing(ApiDevice::getName));
  }
}
