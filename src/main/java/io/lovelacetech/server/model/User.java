package io.lovelacetech.server.model;

import com.google.common.base.Strings;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.converter.AccessLevelConverter;
import io.lovelacetech.server.util.UUIDUtils;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "lovelace")
public class User implements DatabaseModel<User>, ApiModelConvertible<ApiUser> {
  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Convert(converter = AccessLevelConverter.class)
  @Column(name = "access_level", nullable = false)
  private AccessLevel accessLevel;

  @Column(name = "company_id")
  private UUID companyId;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "users_locations", schema = "lovelace",
      joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)},
      inverseJoinColumns = {
          @JoinColumn(name = "location_id", nullable = false, updatable = false)})
  private List<Location> locations;

  @Override
  @Transient
  public ApiUser toApi() {
    return new ApiUser(this);
  }

  @Override
  @Transient
  public boolean hasId() {
    return UUIDUtils.isValidId(id);
  }

  @Override
  @Transient
  public boolean idEquals(UUID otherId) {
    return UUIDUtils.idsEqual(id, otherId);
  }

  @Override
  @Transient
  public void applyUpdate(User other) {
    if (!Strings.isNullOrEmpty(other.email)) {
      email = other.email;
    }

    if (!Strings.isNullOrEmpty(other.username)) {
      username = other.username;
    }

    if (other.accessLevel != null) {
      accessLevel = other.accessLevel;
    }

    if (UUIDUtils.isValidId(other.companyId)) {
      companyId = other.companyId;
    }

    if (!Strings.isNullOrEmpty(other.firstName)) {
      firstName = other.firstName;
    }

    if (!Strings.isNullOrEmpty(other.lastName)) {
      lastName = other.lastName;
    }
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public AccessLevel getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(AccessLevel accessLevel) {
    this.accessLevel = accessLevel;
  }

  public UUID getCompanyId() {
    return companyId;
  }

  public void setCompanyId(UUID companyId) {
    this.companyId = companyId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Transient
  public List<Location> getLocations() {
    return locations;
  }

  @Transient
  public void setLocations(List<Location> locations) {
    this.locations = locations;
  }
}
