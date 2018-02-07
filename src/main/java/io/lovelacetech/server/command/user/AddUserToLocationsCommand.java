package io.lovelacetech.server.command.user;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.user.UserApiResponse;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.RepositoryUtils;
import io.lovelacetech.server.util.UUIDUtils;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddUserToLocationsCommand extends UserCommand<AddUserToLocationsCommand> {
  private ApiUser actingUser;
  private LocationRepository locationRepository;
  private UUID userId;
  private List<UUID> locationIds;

  public AddUserToLocationsCommand setActingUser(ApiUser actingUser) {
    this.actingUser = actingUser;
    return this;
  }

  public AddUserToLocationsCommand setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return this;
  }

  public AddUserToLocationsCommand setUserId(UUID userId) {
    this.userId = userId;
    return this;
  }

  public AddUserToLocationsCommand setLocationIds(List<UUID> locationIds) {
    if (locationIds == null) {
      this.locationIds = new ArrayList<>();
    }

    this.locationIds = locationIds;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && actingUser != null
        && UUIDUtils.isValidId(userId)
        && !locationIds.isEmpty()
        && locationIds.stream().allMatch(UUIDUtils::isValidId)
        && locationRepository != null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public UserApiResponse execute() {
    if (!checkCommand()) {
      return new UserApiResponse().setDefault();
    }

    if (locationIds.isEmpty()) {
      return new UserApiResponse().setInvalidBody();
    }

    User user = getUserRepository().findOne(userId);
    if (user == null) {
      return new UserApiResponse().setNotFound();
    }

    if (!AccessUtils.userCanAccessUser(actingUser, user)) {
      return new UserApiResponse().setAccessDenied();
    }

    List<Location> locations = Lists.newArrayList(locationRepository.findAll(locationIds));
    if (locations == null || locations.isEmpty()) {
      return new UserApiResponse().setNotFound();
    }

    if (!locations.stream().allMatch(
        location -> AccessUtils.userCanAccessLocation(actingUser, location))) {
      return new UserApiResponse().setAccessDenied();
    }

    for (Location location : locations) {
      if (!RepositoryUtils.listContainsRow(user.getLocations(), location)) {
        user.getLocations().add(location);
      }
    }

    user = getUserRepository().save(user);

    return new UserApiResponse()
        .setSuccess()
        .setResponse(user.toApi());
  }
}