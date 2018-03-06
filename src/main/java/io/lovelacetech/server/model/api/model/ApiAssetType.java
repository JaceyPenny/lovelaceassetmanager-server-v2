package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.AssetType;
import io.lovelacetech.server.util.UUIDUtils;
import org.assertj.core.util.Strings;

import java.util.UUID;

public class ApiAssetType extends BaseApiModel<AssetType> {
  /**
   * The default name for a newly created AssetType.
   */
  public static final String DEFAULT_ASSET_TYPE = "Asset";

  private UUID id;
  private UUID companyId;
  private String type;

  public static ApiAssetType getDefault() {
    ApiAssetType defaultAssetType = new ApiAssetType();
    defaultAssetType.setType(DEFAULT_ASSET_TYPE);
    return defaultAssetType;
  }

  public ApiAssetType() {
    id = null;
    companyId = null;
    type = "";
  }

  public ApiAssetType(AssetType assetType) {
    id = assetType.getId();
    companyId = assetType.getCompanyId();
    type = assetType.getType();
  }

  public UUID getId() {
    return id;
  }

  public ApiAssetType setId(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getCompanyId() {
    return companyId;
  }

  public ApiAssetType setCompanyId(UUID companyId) {
    this.companyId = companyId;
    return this;
  }

  public String getType() {
    return type;
  }

  public ApiAssetType setType(String type) {
    this.type = type;
    return this;
  }

  public boolean isEmpty() {
    return !UUIDUtils.isValidId(id)
        && !UUIDUtils.isValidId(companyId)
        && Strings.isNullOrEmpty(type);
  }

  @Override
  public boolean isValid() {
    return UUIDUtils.isValidId(id)
        && UUIDUtils.isValidId(companyId)
        && !Strings.isNullOrEmpty(type);
  }

  @Override
  public AssetType toDatabase() {
    AssetType assetType = new AssetType();

    assetType.setId(id);
    assetType.setCompanyId(companyId);
    assetType.setType(type);

    return assetType;
  }
}
