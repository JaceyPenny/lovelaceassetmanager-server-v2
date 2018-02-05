package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.AssetType;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ApiAssetTypeStringList extends BaseApiModel {
  private List<ApiAssetType> assetTypes;

  public ApiAssetTypeStringList() {
    this.assetTypes = new ArrayList<>();
  }

  public ApiAssetTypeStringList(Iterable<AssetType> assetTypes) {
    this.assetTypes = RepositoryUtils.toApiList(assetTypes);
  }

  public ApiAssetTypeStringList(List<ApiAssetType> assetTypes) {
    this.assetTypes = assetTypes;
  }

  public List<String> getAssetTypes() {
    return assetTypes.stream().map(ApiAssetType::getType).sorted().collect(Collectors.toList());
  }

  public ApiAssetTypeStringList setAssetTypes(List<ApiAssetType> assetTypes) {
    this.assetTypes = assetTypes;
    return this;
  }

  public ApiAssetTypeStringList addAsset(ApiAssetType assetType) {
    assetTypes.add(assetType);
    return this;
  }
}
