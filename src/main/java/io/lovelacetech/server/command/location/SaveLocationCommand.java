package io.lovelacetech.server.command.location;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.location.LocationApiResponse;
import io.lovelacetech.server.repository.LogRepository;
import io.lovelacetech.server.util.*;
import org.springframework.dao.DataIntegrityViolationException;

public class SaveLocationCommand extends LocationCommand<SaveLocationCommand> {
  private ApiLocation location;
  private ApiUser user;

  private LogRepository logRepository;

  public SaveLocationCommand setLocation(ApiLocation location) {
    this.location = location;
    return this;
  }

  public SaveLocationCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  public SaveLocationCommand setLogRepository(LogRepository logRepository) {
    this.logRepository = logRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && location != null
        && user != null
        && logRepository != null;
  }

  @Override
  public LocationApiResponse execute() {
    if (!checkCommand()) {
      return new LocationApiResponse().setDefault();
    }

    if (user.getAccessLevel() == AccessLevel.USER) {
      return new LocationApiResponse().setAccessDenied();
    }

    // If the user supplied "id" with their request, fetch the existing Location
    // for that ID. Otherwise, throw "Not Found"
    Location locationUpdate = location.toDatabase();
    ApiLocation oldLocation = new ApiLocation();
    if (locationUpdate.hasId()) {
      Location existingLocation = getLocationRepository().findOne(locationUpdate.getId());

      if (existingLocation == null) {
        return new LocationApiResponse().setNotFound();
      }

      // If the user cannot access this location due to being:
      //   1. AccessLevel USER
      //   2. AccessLevel ADMIN trying to modify a location from another company
      // then return FORBIDDEN
      if (!AccessUtils.userCanAccessLocation(user, existingLocation)) {
        return new LocationApiResponse().setAccessDenied();
      }

      oldLocation = existingLocation.toApi();
      existingLocation.applyUpdate(locationUpdate);
      locationUpdate = existingLocation;
    }

    // Check if the location's company has been set. If not, default to user's company ID
    if (!UUIDUtils.isValidId(locationUpdate.getCompanyId())) {
      locationUpdate.setCompanyId(user.getCompanyId());
    }

    // The Location must have all required fields before we can proceed.
    if (!locationUpdate.toApi().isValid()) {
      return new LocationApiResponse().setInvalidBody();
    }

    // A location cannot be added to the database if its name and company ID
    // are the same as another row
    Location existingLocationWithInfo = getLocationRepository()
        .findByCompanyIdAndName(locationUpdate.getCompanyId(), locationUpdate.getName());
    if (existingLocationWithInfo != null
        && RepositoryUtils.updateConflictsWithExistingRow(
        locationUpdate,
        existingLocationWithInfo)) {
      return new LocationApiResponse()
          .setConflict()
          .setMessage(Messages.LOCATION_CONFLICTING_NAME);
    }

    // Save the location
    try {
      if (locationUpdate.hasId()) {
        locationUpdate = LogUtils.editLocationAndLog(
            user, oldLocation, locationUpdate.toApi(), getLocationRepository(), logRepository);
      } else {
        locationUpdate = LogUtils.addLocationAndLog(
            user, locationUpdate.toApi(), getLocationRepository(), logRepository);
      }
    } catch (DataIntegrityViolationException exception) {
      return new LocationApiResponse().setBadId();
    }

    return new LocationApiResponse()
        .setSuccess()
        .setResponse(locationUpdate.toApi());
  }
}
