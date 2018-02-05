package io.lovelacetech.server.command.user;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.user.UserApiResponse;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.Messages;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;

import java.util.UUID;

public class AddUserToLocationCommand extends UserCommand<AddUserToLocationCommand> {
  private ApiUser actingUser;
  private LocationRepository locationRepository;
  private UUID userId;
  private UUID locationId;

  public AddUserToLocationCommand setActingUser(ApiUser actingUser) {
    this.actingUser = actingUser;
    return this;
  }

  public AddUserToLocationCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  public AddUserToLocationCommand setUserId(UUID userId) {
    this.userId = userId;
    return this;
  }

  public AddUserToLocationCommand setLocationId(UUID locationId) {
    this.locationId = locationId;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && actingUser != null
        && UUIDUtils.isValidId(userId)
        && UUIDUtils.isValidId(locationId)
        && locationRepository != null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public UserApiResponse execute() {
    if (!checkCommand()) {
      return new UserApiResponse().setDefault();
    }

    User user = getUserRepository().findOne(userId);
    if (user == null) {
      return new UserApiResponse().setNotFound();
    }

    if (!AccessUtils.userCanAccessUser(actingUser, user)) {
      return new UserApiResponse().setAccessDenied();
    }

    Location location = locationRepository.findOne(locationId);
    if (location == null) {
      return new UserApiResponse().setNotFound();
    }

    if (!AccessUtils.userCanAccessLocation(actingUser, location)) {
      return new UserApiResponse().setAccessDenied();
    }

    if (RepositoryUtils.listContainsRow(user.getLocations(), location)) {
      return new UserApiResponse()
          .setConflict()
          .setMessage(Messages.USER_ALREADY_HAS_LOCATION)
          .setResponse(user.toApi());
    }

    user.getLocations().add(location);
    user = getUserRepository().save(user);

    return new UserApiResponse()
        .setSuccess()
        .setResponse(user.toApi());
  }
}
