package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Company;

import java.util.UUID;

public class ApiCompany extends BaseApiModel {
  private UUID id;
  private String name;
  private String phoneNumber;

  public ApiCompany() {
    this.id = new UUID(0, 0);
    this.name = "";
    this.phoneNumber = "";
  }

  public ApiCompany(Company company) {
    this.id = company.getId();
    this.name = company.getName();
    this.phoneNumber = company.getPhoneNumber();
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }
}
