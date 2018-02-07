package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class ApiUser extends BaseApiModel<User> {
  private UUID id;
  private String email;
  private String username;
  private String password;
  private AccessLevel accessLevel;
  private UUID companyId;
  private String firstName;
  private String lastName;

  private List<ApiLocation> locations;

  public ApiUser() {
    this.id = UUIDUtils.empty();
    this.email = "";
    this.username = "";
    this.password = "";
    this.accessLevel = AccessLevel.USER;
    this.companyId = UUIDUtils.empty();
    this.firstName = "";
    this.lastName = "";
    this.locations = new ArrayList<>();
  }

  public ApiUser(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.username = user.getUsername();
    this.password = user.getPassword();
    this.accessLevel = user.getAccessLevel();
    this.companyId = user.getCompanyId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.locations = RepositoryUtils.toApiList(user.getLocations());
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

  public List<ApiLocation> getLocations() {
    return locations;
  }

  public void setLocations(List<ApiLocation> locations) {
    this.locations = locations;
  }

  public static ApiUser fromClaims(LinkedHashMap<String, Object> user) {
    ApiUser result = new ApiUser()
        .setId(UUID.fromString((String) user.get("id")))
        .setAccessLevel(AccessLevel.valueOf((String) user.get("accessLevel")))
        .setEmail((String) user.get("email"))
        .setUsername((String) user.get("username"))
        .setFirstName((String) user.get("firstName"))
        .setLastName((String) user.get("lastName"));

    if (user.get("companyId") != null) {
      result.setCompanyId(UUID.fromString((String) user.get("companyId")));
    }

    return result;
  }

  @Override
  public User toDatabase() {
    User user = new User();

    user.setId(id);
    user.setEmail(email);
    user.setUsername(username);
    user.setPassword(password);
    user.setAccessLevel(accessLevel);
    user.setCompanyId(companyId);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setLocations(RepositoryUtils.toDatabaseList(locations));

    return user;
  }
}
