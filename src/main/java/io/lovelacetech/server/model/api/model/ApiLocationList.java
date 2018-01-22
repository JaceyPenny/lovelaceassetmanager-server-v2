package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.ArrayList;
import java.util.List;

public class ApiLocationList extends BaseApiModel {
  private List<ApiLocation> locations;

  public ApiLocationList() {
    this.locations = new ArrayList<>();
  }

  public ApiLocationList(Iterable<Location> locations) {
    this.locations = RepositoryUtils.toApiList(locations);
  }

  public ApiLocationList(List<ApiLocation> locations) {
    this.locations = locations;
  }

  public List<ApiLocation> getLocations() {
    return locations;
  }

  public ApiLocationList setLocations(List<ApiLocation> locations) {
    this.locations = locations;
    return this;
  }

  public ApiLocationList addLocation(ApiLocation location) {
    locations.add(location);
    return this;
  }
}
