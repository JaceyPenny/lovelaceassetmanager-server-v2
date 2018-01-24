package io.lovelacetech.server.model.api.response.asset;

import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.util.Messages;
import org.springframework.http.HttpStatus;

public class AssetApiResponse extends BaseApiResponse<AssetApiResponse, ApiAsset> {
  public AssetApiResponse setBadId() {
    setStatus(HttpStatus.BAD_REQUEST);
    setMessage(Messages.ASSET_BAD_ID);
    return this;
  }
}
