package io.lovelacetech.server.command.location;

import io.lovelacetech.server.command.BaseCommand;
import io.lovelacetech.server.repository.LocationRepository;

public abstract class LocationCommand<T extends LocationCommand>
    implements BaseCommand {
  private LocationRepository locationRepository;

  public T setLocationRepository(LocationRepository locationRepository) {
    this.locationRepository = locationRepository;
    return (T) this;
  }

  public LocationRepository getLocationRepository() {
    return locationRepository;
  }

  @Override
  public boolean checkCommand() {
    return locationRepository != null;
  }
}
