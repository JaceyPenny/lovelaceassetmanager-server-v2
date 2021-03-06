package io.lovelacetech.server.command.user;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.User;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.List;

public class AddUserToLocationsCommand extends UpdateUserLocationsCommand<AddUserToLocationsCommand> {

  @Override
  public void updateUserForLocations(User user, List<Location> locations) {
    for (Location location : locations) {
      if (!RepositoryUtils.listContainsRow(user.getLocations(), location)) {
        user.getLocations().add(location);
      }
    }
  }
}