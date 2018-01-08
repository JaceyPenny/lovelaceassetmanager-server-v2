package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.UUID;

public class ApiLocation extends BaseApiModel {
  private UUID id;
  private String city;
  private String state;
  private UUID companyId;

  public ApiLocation() {
    this.id = UUIDUtils.empty();
    this.city = "";
    this.state = "";
    this.companyId = UUIDUtils.empty();
  }

  public ApiLocation(Location location) {
    this.id = location.getId();
    this.city = location.getCity();
    this.state = location.getState();
    this.companyId = location.getCompanyId();
  }

  public UUID getId() {
    return id;
  }

  public ApiLocation setId(UUID id) {
    this.id = id;
    return this;
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

  @Override
  public Location toDatabase() {
    Location location = new Location();

    location.setId(UUIDUtils.isValidId(id) ? id : null);
    location.setCity(city);
    location.setState(state);
    location.setCompanyId(UUIDUtils.isValidId(id) ? id : null);

    return location;
  }
}
