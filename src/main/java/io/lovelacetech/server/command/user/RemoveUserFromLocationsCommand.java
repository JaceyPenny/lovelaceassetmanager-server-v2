package io.lovelacetech.server.command.user;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.User;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.List;
import java.util.UUID;

public class RemoveUserFromLocationsCommand extends UpdateUserLocationsCommand<RemoveUserFromLocationsCommand> {
  @Override
  public void updateUserForLocations(User user, List<Location> locations) {
    final List<UUID> locationIds = RepositoryUtils.mapToIds(locations);
    user.getLocations().removeIf(location -> locationIds.contains(location.getId()));
  }
}
