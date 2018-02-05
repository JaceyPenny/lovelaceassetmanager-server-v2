package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.AssetType;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.ArrayList;
import java.util.List;

public class ApiAssetTypeList extends BaseApiModel {
  private List<ApiAssetType> assetTypes;

  public ApiAssetTypeList() {
    this.assetTypes = new ArrayList<>();
  }

  public ApiAssetTypeList(Iterable<AssetType> assetTypes) {
    this.assetTypes = RepositoryUtils.toApiList(assetTypes);
  }

  public ApiAssetTypeList(List<ApiAssetType> assetTypes) {
    this.assetTypes = assetTypes;
  }

  public List<ApiAssetType> getAssetTypes() {
    return assetTypes;
  }

  public ApiAssetTypeList setAssetTypes(List<ApiAssetType> assetTypes) {
    this.assetTypes = assetTypes;
    return this;
  }

  public ApiAssetTypeList addAsset(ApiAssetType assetType) {
    assetTypes.add(assetType);
    return this;
  }
}
