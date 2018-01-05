package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.ApiModelConvertible;
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

  @Override
  public String toString() {
    return "{ \"id\": \"" + this.id + "\", \"name\": \"" + name + "\", \"phoneNumber\": \"" + phoneNumber + "\" }";
  }

  @Override
  public Company toDatabase() {
    Company company = new Company();

    company.setId(id.equals(new UUID(0, 0)) ? null : id);
    company.setName(name);
    company.setPhoneNumber(phoneNumber);

    return company;
  }
}
