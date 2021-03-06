package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ApiAssetList extends BaseApiModel {
  private List<ApiAsset> assets;

  public ApiAssetList() {
    this.assets = new ArrayList<>();
  }

  public ApiAssetList(Iterable<Asset> assets) {
    this.assets = RepositoryUtils.toApiList(assets);
  }

  public ApiAssetList(List<ApiAsset> assets) {
    this.assets = assets;
  }

  public List<ApiAsset> getAssets() {
    sort();
    return assets;
  }

  public ApiAssetList setAssets(List<ApiAsset> assets) {
    this.assets = assets;
    return this;
  }

  public ApiAssetList addAsset(ApiAsset asset) {
    assets.add(asset);
    return this;
  }

  public void sort() {
    assets.sort(Comparator.comparing(ApiAsset::getName));
  }
}
