package io.lovelacetech.server.model.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApiLocationIdList {
  List<UUID> locationIds;

  public ApiLocationIdList() {
    locationIds = new ArrayList<>();
  }

  public ApiLocationIdList(List<UUID> locationIds) {
    this.locationIds = locationIds;
  }

  public List<UUID> getLocationIds() {
    return locationIds;
  }

  public ApiLocationIdList setLocationIds(List<UUID> locationIds) {
    this.locationIds = locationIds;
    return this;
  }
}
