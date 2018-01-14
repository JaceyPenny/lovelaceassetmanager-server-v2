package io.lovelacetech.server.command.location;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.location.LocationApiResponse;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.Messages;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

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
      } else {
        existingLocation.applyUpdate(locationUpdate);
        locationUpdate = existingLocation;
      }
    }

    // The user must belong to the company for the location they are modifying.
    // The user cannot set a location's company to a different company
    // Super users can override this check
    if (UUIDUtils.isValidId(locationUpdate.getCompanyId())
        && !UUIDUtils.idsEqual(locationUpdate.getCompanyId(), user.getCompanyId())
        && !AuthenticationUtils.userIsSuper(user)) {
      throw new AccessDeniedException(Messages.ACCESS_DENIED);
    } else if (!UUIDUtils.isValidId(locationUpdate.getCompanyId())) {
      locationUpdate.setCompanyId(user.getCompanyId());
    }

    // The Location must have all required fields before we can proceed.
    if (!locationUpdate.toApi().isValid()) {
      System.out.println(locationUpdate.toApi());
      return new LocationApiResponse()
          .setStatus(HttpStatus.BAD_REQUEST)
          .setMessage(Messages.INVALID_BODY);
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
          .setStatus(HttpStatus.CONFLICT)
          .setMessage(Messages.LOCATION_CONFLICTING_NAME);
    }

    // Save the location
    locationUpdate = getLocationRepository().save(locationUpdate);

    return new LocationApiResponse()
        .setSuccess()
        .setResponse(locationUpdate.toApi());
  }
}
