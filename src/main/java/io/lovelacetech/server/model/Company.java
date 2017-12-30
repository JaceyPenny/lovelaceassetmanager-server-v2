package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.model.ApiCompany;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "company", schema = "lovelace")
public class Company implements ApiModelConvertible<ApiCompany> {
  @Id
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Column(name="name", unique = true, nullable = false)
  private String name;

  @Column(name = "phone_number", unique = true, nullable = false)
  private String phoneNumber;

  public ApiCompany toApi() {
    return new ApiCompany(this);
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
