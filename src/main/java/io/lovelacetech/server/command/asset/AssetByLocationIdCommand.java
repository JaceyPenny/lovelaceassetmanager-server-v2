package io.lovelacetech.server.command.asset;

import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.api.model.ApiAssetList;
import io.lovelacetech.server.model.api.response.BaseApiResponse;
import io.lovelacetech.server.model.api.response.asset.AssetListApiResponse;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.List;
import java.util.UUID;

public class AssetByLocationIdCommand extends AssetCommand<AssetByLocationIdCommand> {
  private UUID locationId;

  public AssetByLocationIdCommand setLocationId(UUID locationId) {
    this.locationId = locationId;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand() && UUIDUtils.isValidId(locationId);
  }

  @Override
  public AssetListApiResponse execute() {
    if (!checkCommand()) {
      return new AssetListApiResponse().setDefault();
    }

    List<ApiAsset> assets = RepositoryUtils.toApiList(
        getAssetRepository().findAllByLocationId(locationId));

    return new AssetListApiResponse()
        .setSuccess()
        .setResponse(new ApiAssetList().setAssets(assets));
  }
}
