package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.util.UUIDUtils;
import org.assertj.core.util.Strings;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "location", schema = "lovelace")
public class Location implements DatabaseModel<Location>, ApiModelConvertible<ApiLocation> {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, updatable = false)
  private UUID id;

  @Column(name = "city", nullable = false)
  private String city;

  @Column(name = "state", nullable = false)
  private String state;

  @Column(name = "company_id", nullable = false)
  private UUID companyId;


  @Override
  public ApiLocation toApi() {
    return new ApiLocation(this);
  }

  @Override
  public boolean hasId() {
    return UUIDUtils.isValidId(id);
  }

  @Override
  public boolean idEquals(UUID otherId) {
    return UUIDUtils.idsEqual(id, otherId);
  }

  @Override
  public void applyUpdate(Location other) {
    if (!Strings.isNullOrEmpty(other.city)) {
      city = other.city;
    }

    if (!Strings.isNullOrEmpty(other.state)) {
      state = other.state;
    }

    if (UUIDUtils.isValidId(other.companyId)) {
      companyId = other.companyId;
    }
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public UUID getCompanyId() {
    return companyId;
  }

  public void setCompanyId(UUID companyId) {
    this.companyId = companyId;
  }
}
