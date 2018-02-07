package io.lovelacetech.server.command.user;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.User;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.user.UserApiResponse;
import io.lovelacetech.server.repository.LocationRepository;
import io.lovelacetech.server.util.AccessUtils;
import io.lovelacetech.server.util.UUIDUtils;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class UpdateUserLocationsCommand<T extends UpdateUserLocationsCommand> extends UserCommand<T> {
  private ApiUser actingUser;
  private LocationRepository locationRepository;
  private UUID userId;
  private List<UUID> locationIds;

  public T setActingUser(ApiUser actingUser) {
    this.actingUser = actingUser;
    return (T) this;
  }

  public T setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return (T) this;
  }

  public T setUserId(UUID userId) {
    this.userId = userId;
    return (T) this;
  }

  public T setLocationIds(List<UUID> locationIds) {
    if (locationIds == null) {
      this.locationIds = new ArrayList<>();
    }

    this.locationIds = locationIds;
    return (T) this;
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

  public abstract void updateUserForLocations(User user, List<Location> locations);

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

    List<Location> locations = Lists.newArrayList(locationRepository.findAll(locationIds));
    if (locations == null || locations.isEmpty()) {
      return new UserApiResponse().setNotFound();
    }

    if (!locations.stream().allMatch(
        location -> AccessUtils.userCanAccessLocation(actingUser, location))) {
      return new UserApiResponse().setAccessDenied();
    }

    updateUserForLocations(user, locations);
    user = getUserRepository().save(user);

    return new UserApiResponse()
        .setSuccess()
        .setResponse(user.toApi());
  }
}
