package io.lovelacetech.server.command.user;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.User;

import java.util.List;

public class SetUserToLocationsCommand extends UpdateUserLocationsCommand<SetUserToLocationsCommand> {
  @Override
  public void updateUserForLocations(User user, List<Location> locations) {
    user.setLocations(locations);
  }
}
