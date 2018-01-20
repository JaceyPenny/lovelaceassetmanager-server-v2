package io.lovelacetech.server.command.location;

import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.model.ApiLocationList;
import io.lovelacetech.server.model.api.response.location.LocationListApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.util.LoaderUtils;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.List;
import java.util.UUID;

public class LocationsByCompanyIdCommand extends LocationCommand<LocationsByCompanyIdCommand> {
  private UUID companyId;
  private boolean filled = false;

  private DeviceRepository deviceRepository;
  private AssetRepository assetRepository;

  public LocationsByCompanyIdCommand setCompanyId(UUID companyId) {
    this.companyId = companyId;
    return this;
  }

  public LocationsByCompanyIdCommand setFilled(boolean filled) {
    this.filled = filled;
    return this;
  }

  public LocationsByCompanyIdCommand setDeviceRepository(
      DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return this;
  }

  public LocationsByCompanyIdCommand setAssetRepository(
      AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && UUIDUtils.isValidId(companyId)
        && (!filled || (deviceRepository != null && assetRepository != null));
  }

  @Override
  public LocationListApiResponse execute() {
    if (!checkCommand()) {
      return new LocationListApiResponse().setDefault();
    }

    List<ApiLocation> locations = RepositoryUtils.toApiList(
        getLocationRepository().findAllByCompanyId(companyId));

    if (filled) {
      LoaderUtils.populateLocations(locations, deviceRepository, assetRepository);
    }

    return new LocationListApiResponse()
        .setSuccess()
        .setResponse(new ApiLocationList().setLocations(locations));
  }
}
