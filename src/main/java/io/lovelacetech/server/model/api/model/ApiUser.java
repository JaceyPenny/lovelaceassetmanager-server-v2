package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.enums.AccessLevel;

import java.util.UUID;

public class ApiUser extends BaseApiModel {
  private UUID id;
  private String email;
  private String username;
  private AccessLevel accessLevel;
  private UUID companyId;
  private String firstName;
  private String lastName;

  public ApiUser() {
    this.id = new UUID(0, 0);
    this.email = "";
    this.username = "";
    this.accessLevel = AccessLevel.USER;
    this.companyId = new UUID(0, 0);
    this.firstName = "";
    this.lastName = "";
  }
  
  public ApiUser(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.username = user.getUsername();
    this.accessLevel = AccessLevel.fromInt(user.getAccessLevel());
    this.companyId = user.getCompanyId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
  }

  public UUID getId() {
    return id;
  }

  public ApiUser setId(UUID id) {
    this.id = id;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public ApiUser setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public ApiUser setUsername(String username) {
    this.username = username;
    return this;
  }

  public AccessLevel getAccessLevel() {
    return accessLevel;
  }

  public ApiUser setAccessLevel(AccessLevel accessLevel) {
    this.accessLevel = accessLevel;
    return this;
  }

  public UUID getCompanyId() {
    return companyId;
  }

  public ApiUser setCompanyId(UUID companyId) {
    this.companyId = companyId;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public ApiUser setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public ApiUser setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }
}