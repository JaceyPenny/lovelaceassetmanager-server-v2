package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApiCompany extends BaseApiModel<Company> {
  private UUID id;
  private String name;
  private String phoneNumber;

  private List<ApiLocation> locations;

  public ApiCompany() {
    this.id = null;
    this.name = "";
    this.phoneNumber = "";
    this.locations = new ArrayList<>();
  }

  public ApiCompany(Company company) {
    this.id = company.getId();
    this.name = company.getName();
    this.phoneNumber = company.getPhoneNumber();
    this.locations = new ArrayList<>();
  }

  public ApiCompany setId(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public ApiCompany setName(String name) {
    this.name = name;
    return this;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public ApiCompany setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  public List<ApiLocation> getLocations() {
    return locations;
  }

  public ApiCompany setLocations(List<ApiLocation> locations) {
    this.locations = locations;
    return this;
  }

  public ApiCompany addLocation(ApiLocation location) {
    this.locations.add(location);
    return this;
  }

  @Override
  public Company toDatabase() {
    Company company = new Company();

    company.setId(UUIDUtils.isValidId(id) ? id : null);
    company.setName(name);
    company.setPhoneNumber(phoneNumber);

    return company;
  }
}
