package io.lovelacetech.server.command.asset;

import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.api.model.ApiAssetList;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.asset.AssetListApiResponse;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.List;
import java.util.UUID;

public class AssetByLocationIdCommand extends AssetCommand<AssetByLocationIdCommand> {
  private UUID locationId;
  private ApiUser user;

  private LocationRepository locationRepository;

  public AssetByLocationIdCommand setLocationId(UUID locationId) {
    this.locationId = locationId;
    return this;
  }

  public AssetByLocationIdCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public AssetByLocationIdCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && UUIDUtils.isValidId(locationId)
        && user != null
        && locationRepository != null;
  }

  @Override
  public AssetListApiResponse execute() {
    if (!checkCommand()) {
      return new AssetListApiResponse().setDefault();
    }

    if (!AccessUtils.userCanAccessLocation(user, locationId, locationRepository)) {
      return new AssetListApiResponse().setAccessDenied();
    }

    List<ApiAsset> assets = RepositoryUtils.toApiList(
        getAssetRepository().findAllByLocationId(locationId));

    return new AssetListApiResponse()
        .setSuccess()
        .setResponse(new ApiAssetList().setAssets(assets));
  }
}
