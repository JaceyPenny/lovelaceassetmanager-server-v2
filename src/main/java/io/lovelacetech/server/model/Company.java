package io.lovelacetech.server.model;

import com.google.common.base.Strings;
import io.lovelacetech.server.model.api.model.ApiCompany;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "company", schema = "lovelace")
public class Company implements DatabaseModel<Company>, ApiModelConvertible<ApiCompany> {
  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "name", unique = true, nullable = false)
  private String name;

  @Column(name = "phone_number", unique = true, nullable = false)
  private String phoneNumber;

  @Transient
  public ApiCompany toApi() {
    return new ApiCompany(this);
  }

  @Override
  @Transient
  public boolean hasId() {
    return !idEquals(null) && !idEquals(new UUID(0, 0));
  }

  @Override
  @Transient
  public boolean idEquals(UUID otherId) {
    return id == otherId || (id != null && id.equals(otherId));
  }

  @Override
  @Transient
  public void applyUpdate(Company other) {
    if (!Strings.isNullOrEmpty(other.name)) {
      name = other.name;
    }

    if (!Strings.isNullOrEmpty(other.phoneNumber)) {
      phoneNumber = other.phoneNumber;
    }
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
