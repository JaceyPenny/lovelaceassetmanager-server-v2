package io.lovelacetech.server.util;

import io.lovelacetech.server.model.Location;
import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.model.ApiUser;

public class AccessUtils {
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
}
