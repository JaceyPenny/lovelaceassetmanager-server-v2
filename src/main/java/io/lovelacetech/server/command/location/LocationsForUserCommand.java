package io.lovelacetech.server.command.location;

import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.model.ApiLocationList;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.location.LocationListApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.util.LoaderUtils;

import java.util.List;

public class LocationsForUserCommand extends LocationCommand<LocationsForUserCommand> {
  private ApiUser user;
  private boolean filled = false;

  private DeviceRepository deviceRepository;
  private AssetRepository assetRepository;

  public LocationsForUserCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public LocationsForUserCommand setFilled(boolean filled) {
    this.filled = filled;
    return this;
  }

  public LocationsForUserCommand setDeviceRepository(
      DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return this;
  }

  public LocationsForUserCommand setAssetRepository(
      AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && (!filled || (deviceRepository != null && assetRepository != null));
  }

  @Override
  public LocationListApiResponse execute() {
    if (!checkCommand()) {
      return new LocationListApiResponse().setDefault();
    }

    List<ApiLocation> locations = LoaderUtils.getLocationsForUser(user, getLocationRepository());

    if (filled) {
      LoaderUtils.populateLocations(locations, deviceRepository, assetRepository);
    }

    return new LocationListApiResponse()
        .setSuccess()
        .setResponse(new ApiLocationList(locations));
  }
}
