package io.lovelacetech.server.model.api.response.device;

import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.util.Messages;
import org.springframework.http.HttpStatus;

public class DeviceApiResponse extends BaseApiResponse<DeviceApiResponse, ApiDevice> {
  public DeviceApiResponse setBadId() {
    setStatus(HttpStatus.BAD_REQUEST);
    setMessage(Messages.DEVICE_BAD_ID);
    return this;
  }
}
