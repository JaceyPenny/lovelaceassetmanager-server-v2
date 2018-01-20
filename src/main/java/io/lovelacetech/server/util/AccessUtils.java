package io.lovelacetech.server.util;

import io.lovelacetech.server.model.Device;
import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;

import java.util.UUID;

public class AccessUtils {
  public static boolean userCanAccessLocation(
      ApiUser user,
      UUID locationId,
      LocationRepository locationRepository) {
    Location fetchedLocation = locationRepository.findOne(locationId);
    return fetchedLocation != null && userCanAccessLocation(user, fetchedLocation);
  }

  public static boolean userCanAccessLocation(ApiUser user, Location location) {
    if (AuthenticationUtils.userIsSuper(user)
        || (AuthenticationUtils.userIsAdmin(user)
            && location.getCompanyId().equals(user.getCompanyId()))) {
      return true;
    }

    for (ApiLocation searchLocation : user.getLocations()) {
      if (searchLocation.getId().equals(location.getId())) {
        return true;
      }
    }

    return false;
  }

  public static boolean userCanAccessDevice(
      ApiUser user,
      UUID deviceId,
      DeviceRepository deviceRepository,
      LocationRepository locationRepository) {
    Device fetchedDevice = deviceRepository.findOne(deviceId);
    return deviceId != null && userCanAccessDevice(user, fetchedDevice, locationRepository);
  }

  public static boolean userCanAccessDevice(
      ApiUser user,
      Device device,
      LocationRepository locationRepository) {
    return userCanAccessLocation(user, device.getLocationId(), locationRepository);
  }
}
