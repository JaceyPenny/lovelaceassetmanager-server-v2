package io.lovelacetech.server.command.company;

import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.company.CompanyApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.LoaderUtils;

public class CompaniesForUserCommand extends CompanyCommand<CompaniesForUserCommand> {
  private ApiUser user;
  private boolean filled;
  private LocationRepository locationRepository;
  private DeviceRepository deviceRepository;
  private AssetRepository assetRepository;

  public ApiUser getUser() {
    return user;
  }

  public CompaniesForUserCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public CompaniesForUserCommand setFilled(boolean filled) {
    this.filled = filled;
    return this;
  }

  public CompaniesForUserCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  public CompaniesForUserCommand setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return this;
  }

  public CompaniesForUserCommand setAssetRepository(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && (!filled
            || (locationRepository != null && deviceRepository != null && assetRepository != null));
  }

  @Override
  public CompanyApiResponse execute() {
    if (!checkCommand()) {
      return new CompanyApiResponse().setDefault();
    }

    ApiCompany company = getCompanyRepository().findOne(user.getCompanyId()).toApi();

    if (filled) {
      LoaderUtils.populateCompany(company, locationRepository, deviceRepository, assetRepository);
    }

    return new CompanyApiResponse()
        .setSuccess()
        .setResponse(company);
  }
}
