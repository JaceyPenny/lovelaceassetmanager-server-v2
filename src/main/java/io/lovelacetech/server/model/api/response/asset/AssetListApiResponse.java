package io.lovelacetech.server.model.api.response.asset;

import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.api.model.ApiAssetList;
import io.lovelacetech.server.model.api.response.BaseApiResponse;

public class AssetListApiResponse extends BaseApiResponse<AssetListApiResponse, ApiAssetList> {
  public AssetListApiResponse setResponse(Iterable<Asset> assets) {
    return super.setResponse(new ApiAssetList(assets));
  }
}
