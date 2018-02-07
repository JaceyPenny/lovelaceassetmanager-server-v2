package io.lovelacetech.server.command.device;

import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.model.ApiDeviceList;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.device.DeviceListApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.CompanyRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.LoaderUtils;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.List;
import java.util.UUID;

public class DevicesForUserCommand extends DeviceCommand<DevicesForUserCommand> {
  private ApiUser user;
  private boolean filled;
  private CompanyRepository companyRepository;
  private LocationRepository locationRepository;
  private AssetRepository assetRepository;

  public DevicesForUserCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public DevicesForUserCommand setFilled(boolean filled) {
    this.filled = filled;
    return this;
  }

  public DevicesForUserCommand setCompanyRepository(CompanyRepository companyRepository) {
    this.companyRepository = companyRepository;
    return this;
  }

  public DevicesForUserCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  public DevicesForUserCommand setAssetRepository(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && companyRepository != null
        && locationRepository != null;
  }

  @Override
  public DeviceListApiResponse execute() {
    if (!checkCommand()) {
      return new DeviceListApiResponse().setDefault();
    }

    if (AuthenticationUtils.userIsSuper(user)) {
      return new DeviceListApiResponse()
          .setSuccess()
          .setResponse(getDeviceRepository().findAll());
    }

    List<UUID> locationIds;

    if (AuthenticationUtils.userIsAdmin(user)) {
      Company company = companyRepository.findOne(user.getCompanyId());
      if (company == null) {
        return new DeviceListApiResponse().setNotFound();
      }

      List<Location> locations = locationRepository.findAllByCompanyId(company.getId());
      if (locations == null || locations.isEmpty()) {
        return new DeviceListApiResponse().setNotFound();
      }

      locationIds = RepositoryUtils.mapToIds(locations);
    } else {
      List<Location> locations = RepositoryUtils.toDatabaseList(user.getLocations());
      locationIds = RepositoryUtils.mapToIds(locations);
    }

    List<Device> devices = getDeviceRepository().findAllByLocationIdIn(locationIds);

    if (devices == null || devices.isEmpty()) {
      return new DeviceListApiResponse().setNotFound();
    }

    List<ApiDevice> apiDevices = RepositoryUtils.toApiList(devices);

    if (filled) {
      LoaderUtils.populateDevices(apiDevices, assetRepository);
    }

    return new DeviceListApiResponse()
        .setSuccess()
        .setResponse(new ApiDeviceList(apiDevices));
  }
}
