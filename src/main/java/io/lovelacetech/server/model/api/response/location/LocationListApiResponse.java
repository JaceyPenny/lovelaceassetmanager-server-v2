package io.lovelacetech.server.model.api.response.location;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.model.ApiLocationList;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.util.Messages;
import org.springframework.http.HttpStatus;

public class LocationListApiResponse extends BaseApiResponse<LocationListApiResponse, ApiLocationList> {
  public LocationListApiResponse setNotFound() {
    setStatus(HttpStatus.NOT_FOUND);
    setMessage(Messages.NO_LOCATIONS_FOUND);
    super.setResponse(null);
    return this;
  }

  public LocationListApiResponse setResponse(Iterable<Location> locations) {
    setResponse(new ApiLocationList(locations));
    return this;
  }
}
