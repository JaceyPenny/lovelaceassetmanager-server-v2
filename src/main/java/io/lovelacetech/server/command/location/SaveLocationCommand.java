package io.lovelacetech.server.command.location;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.location.LocationApiResponse;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.Messages;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;
import org.springframework.dao.DataIntegrityViolationException;

public class SaveLocationCommand extends LocationCommand<SaveLocationCommand> {
  private ApiLocation location;
  private ApiUser user;

  public SaveLocationCommand setLocation(ApiLocation location) {
    this.location = location;
    return this;
  }

  public SaveLocationCommand setUser(ApiUser user) {
    this.user = user;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand() && location != null && user != null;
  }

  @Override
  public LocationApiResponse execute() {
    if (!checkCommand()) {
      return new LocationApiResponse().setDefault();
    }

    // If the user supplied "id" with their request, fetch the existing Location
    // for that ID. Otherwise, throw "Not Found"
    Location locationUpdate = location.toDatabase();
    if (locationUpdate.hasId()) {
      Location existingLocation = getLocationRepository().findOne(locationUpdate.getId());

      if (existingLocation == null) {
        return new LocationApiResponse().setNotFound();
      }

      // If the user cannot access this location due to being:
      //   1. AccessLevel USER without permissions for this location
      //   2. AccessLevel ADMIN trying to modify a location from another company
      // then return FORBIDDEN
      if (!AccessUtils.userCanAccessLocation(user, existingLocation)) {
        return new LocationApiResponse().setAccessDenied();
      }

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
      locationUpdate = getLocationRepository().save(locationUpdate);
    } catch (DataIntegrityViolationException exception) {
      return new LocationApiResponse().setBadId();
    }

    return new LocationApiResponse()
        .setSuccess()
        .setResponse(locationUpdate.toApi());
  }
}
