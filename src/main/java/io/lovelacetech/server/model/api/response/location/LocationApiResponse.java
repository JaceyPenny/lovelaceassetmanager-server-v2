package io.lovelacetech.server.model.api.response.location;

import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.util.Messages;
import org.springframework.http.HttpStatus;

public class LocationApiResponse extends BaseApiResponse<LocationApiResponse, ApiLocation> {
  public LocationApiResponse setBadId() {
    setStatus(HttpStatus.BAD_REQUEST);
    setMessage(Messages.LOCATION_BAD_ID);
    return this;
  }
}
