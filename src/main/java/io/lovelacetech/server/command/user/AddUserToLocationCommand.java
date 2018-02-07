package io.lovelacetech.server.command.user;

import java.util.Collections;
import java.util.UUID;

public class AddUserToLocationCommand extends AddUserToLocationsCommand {
  public AddUserToLocationCommand setLocationId(UUID locationId) {
    setLocationIds(Collections.singletonList(locationId));
    return this;
  }
}
