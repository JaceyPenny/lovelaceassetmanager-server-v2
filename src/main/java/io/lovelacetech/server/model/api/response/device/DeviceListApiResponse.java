package io.lovelacetech.server.model.api.response.device;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.api.model.ApiDeviceList;
import io.lovelacetech.server.model.api.response.BaseApiResponse;

public class DeviceListApiResponse extends BaseApiResponse<DeviceListApiResponse, ApiDeviceList> {
  public DeviceListApiResponse setResponse(Iterable<Device> devices) {
    return super.setResponse(new ApiDeviceList(devices));
  }

  @Override
  public ApiDeviceList getResponse() {
    super.getResponse().sort();
    return super.getResponse();
  }
}
