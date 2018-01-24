package io.lovelacetech.server.command.location;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.location.LocationApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.LoaderUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.UUID;

public class LocationByLocationIdCommand extends LocationCommand<LocationByLocationIdCommand> {
  private ApiUser user;
  private UUID locationId;
  private boolean filled;

  private DeviceRepository deviceRepository;
  private AssetRepository assetRepository;

  public LocationByLocationIdCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public LocationByLocationIdCommand setLocationId(UUID locationId) {
    this.locationId = locationId;
    return this;
  }

  public LocationByLocationIdCommand setFilled(boolean filled) {
    this.filled = filled;
    return this;
  }

  public LocationByLocationIdCommand setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return this;
  }

  public LocationByLocationIdCommand setAssetRepository(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && UUIDUtils.isValidId(locationId)
        && (!filled || (deviceRepository != null && assetRepository != null));
  }

  @Override
  public LocationApiResponse execute() {
    if (!checkCommand()) {
      return new LocationApiResponse().setDefault();
    }

    Location location = getLocationRepository().findOne(locationId);
    if (location == null) {
      return new LocationApiResponse().setNotFound();
    }

    if (!AuthenticationUtils.userIsSuper(user)
        && !AccessUtils.userCanAccessLocation(user, location)) {
      return new LocationApiResponse().setAccessDenied();
    }

    ApiLocation apiLocation = location.toApi();
    if (filled) {
      LoaderUtils.populateLocation(apiLocation, deviceRepository, assetRepository);
    }

    return new LocationApiResponse()
        .setSuccess()
        .setResponse(apiLocation);
  }
}
