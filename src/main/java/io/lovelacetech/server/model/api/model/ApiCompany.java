package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.List;
import java.util.UUID;

public class ApiCompany extends BaseApiModel {
  private UUID id;
  private String name;
  private String phoneNumber;

  private List<Location> locations;

  public ApiCompany() {
    this.id = UUIDUtils.empty();
    this.name = "";
    this.phoneNumber = "";
  }

  public ApiCompany(Company company) {
    this.id = company.getId();
    this.name = company.getName();
    this.phoneNumber = company.getPhoneNumber();
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

  public List<Location> getLocations() {
    return locations;
  }

  public ApiCompany setLocations(List<Location> locations) {
    this.locations = locations;
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
