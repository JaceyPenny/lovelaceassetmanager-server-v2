package io.lovelacetech.server.command.company;

import io.lovelacetech.server.model.Company;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.company.CompanyApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.LoaderUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.UUID;

public class CompanyByCompanyIdCommand extends CompanyCommand<CompanyByCompanyIdCommand> {
  private ApiUser user;
  private UUID companyId;
  private boolean filled;

  private LocationRepository locationRepository;
  private DeviceRepository deviceRepository;
  private AssetRepository assetRepository;

  public CompanyByCompanyIdCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public CompanyByCompanyIdCommand setCompanyId(UUID companyId) {
    this.companyId = companyId;
    return this;
  }

  public CompanyByCompanyIdCommand setFilled(boolean filled) {
    this.filled = filled;
    return this;
  }

  public CompanyByCompanyIdCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  public CompanyByCompanyIdCommand setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return this;
  }

  public CompanyByCompanyIdCommand setAssetRepository(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && user != null
        && UUIDUtils.isValidId(companyId)
        && (!filled
            || (locationRepository != null && deviceRepository != null && assetRepository != null));
  }

  @Override
  public CompanyApiResponse execute() {
    if (!checkCommand()) {
      return new CompanyApiResponse().setDefault();
    }

    if (!AuthenticationUtils.userIsSuper(user)
        && !UUIDUtils.idsEqual(user.getCompanyId(), companyId)) {
      return new CompanyApiResponse().setAccessDenied();
    }

    Company company = getCompanyRepository().findOne(companyId);
    if (company == null) {
      return new CompanyApiResponse().setNotFound();
    }

    ApiCompany apiCompany = company.toApi();

    if (filled) {
      LoaderUtils.populateCompany(
          apiCompany, locationRepository, deviceRepository, assetRepository);
    }

    return new CompanyApiResponse()
        .setSuccess()
        .setResponse(company.toApi());
  }
}
