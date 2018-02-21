package io.lovelacetech.server.util;

import com.google.common.collect.Lists;
import io.lovelacetech.server.model.*;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.repository.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

  public static boolean userCanAccessLocations(ApiUser user, List<Location> locations) {
    return locations.stream().allMatch(location -> userCanAccessLocation(user, location));
  }

  public static boolean userCanAccessDevice(
      ApiUser user,
      UUID deviceId,
      DeviceRepository deviceRepository,
      LocationRepository locationRepository) {
    Device fetchedDevice = deviceRepository.findOne(deviceId);
    return fetchedDevice != null && userCanAccessDevice(user, fetchedDevice, locationRepository);
  }

  public static boolean userCanAccessDevice(
      ApiUser user,
      Device device,
      LocationRepository locationRepository) {
    return userCanAccessLocation(user, device.getLocationId(), locationRepository);
  }

  public static boolean userCanAccessDevices(ApiUser user, List<Device> devices, LocationRepository locationRepository) {
    List<UUID> locationIds = devices.stream().map(Device::getLocationId).collect(Collectors.toList());
    List<Location> locations = Lists.newArrayList(locationRepository.findAll(locationIds));
    return userCanAccessLocations(user, locations);
  }

  public static boolean userCanAccessAsset(
      ApiUser user,
      UUID assetId,
      AssetRepository assetRepository,
      DeviceRepository deviceRepository,
      LocationRepository locationRepository) {
    Asset fetchedAsset = assetRepository.findOne(assetId);
    return assetId != null
        && userCanAccessAsset(user, fetchedAsset, deviceRepository, locationRepository);
  }

  public static boolean userCanAccessAsset(
      ApiUser user,
      Asset asset,
      DeviceRepository deviceRepository,
      LocationRepository locationRepository) {
    return userCanAccessDevice(user, asset.getHomeId(), deviceRepository, locationRepository);
  }

  public static boolean userCanAccessUser(
      ApiUser user,
      UUID userId,
      UserRepository userRepository) {
    User fetchedUser = userRepository.findOne(userId);
    return fetchedUser != null && userCanAccessUser(user, fetchedUser);
  }

  public static boolean userCanAccessUser(ApiUser user, User otherUser) {
    return AuthenticationUtils.userIsSuper(user)
        || (user.getAccessLevel() == AccessLevel.ADMIN
        && UUIDUtils.idsEqual(user.getCompanyId(), otherUser.getCompanyId())
        && otherUser.getAccessLevel() != AccessLevel.SUPER);
  }

  public static boolean userCanAccessNotification(
      ApiUser user,
      UUID notificationId,
      NotificationRepository notificationRepository,
      UserRepository userRepository) {
    Notification notification = notificationRepository.findOne(notificationId);
    return notification != null && userCanAccessNotification(user, notification, userRepository);
  }

  public static boolean userCanAccessNotification(
      ApiUser user,
      Notification notification,
      UserRepository userRepository) {
    return AuthenticationUtils.userIsSuper(user)
        || UUIDUtils.idsEqual(user.getId(), notification.getUserId())
        || (AuthenticationUtils.userIsAdmin(user) && userCanAccessUser(user, notification.getUserId(), userRepository));
  }
}
