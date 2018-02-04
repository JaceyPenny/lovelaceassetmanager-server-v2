package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.model.ApiAssetType;
import io.lovelacetech.server.util.UUIDUtils;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "asset_type", schema = "lovelace")
public class AssetType implements DatabaseModel<AssetType>, ApiModelConvertible<ApiAssetType> {

  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  private UUID id;

  @Column(name = "company_id", nullable = false, updatable = false)
  private UUID companyId;

  @Column(name = "type", nullable = false, updatable = false)
  private String type;


  @Override
  public ApiAssetType toApi() {
    return new ApiAssetType(this);
  }

  @Override
  public UUID getId() {
    return id;
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
  public void applyUpdate(AssetType other) {
    // Updates are not allowed on AssetTypes
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getCompanyId() {
    return companyId;
  }

  public void setCompanyId(UUID companyId) {
    this.companyId = companyId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
