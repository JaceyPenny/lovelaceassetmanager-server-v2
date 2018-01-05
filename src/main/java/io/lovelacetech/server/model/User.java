package io.lovelacetech.server.model;

import com.google.common.base.Strings;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiUser;

import javax.persistence.*;
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

  @Column(name = "access_level", nullable = false)
  private int accessLevel;

  @Column(name = "company_id")
  private UUID companyId;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Transient
  public ApiUser toApi() {
    return new ApiUser(this);
  }

  @Override
  @Transient
  public boolean hasId() {
    return !idEquals(new UUID(0, 0));
  }

  @Override
  @Transient
  public boolean idEquals(UUID otherId) {
    return id.equals(otherId);
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

    if (!Strings.isNullOrEmpty(other.password)) {
      password = other.password;
    }

    if (other.accessLevel != AccessLevel.DEFAULT.toInt()) {
      accessLevel = other.accessLevel;
    }

    if (other.companyId != null) {
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

  public int getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(int accessLevel) {
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
}
