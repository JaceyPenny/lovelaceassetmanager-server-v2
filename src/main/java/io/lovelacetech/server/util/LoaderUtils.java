package io.lovelacetech.server.util;

import com.google.common.base.Functions;
import io.lovelacetech.server.model.*;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.enums.AssetStatus;
import io.lovelacetech.server.model.api.model.*;
import io.lovelacetech.server.repository.*;
import org.springframework.data.rest.core.util.MapUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LoaderUtils {
  /**
   * Uses an AssetRepository to populate the "assets" list of "device".
   * <br>
   * Generates 1 PostgreSQL query.
   *
   * @param device          the device to populate
   * @param assetRepository the AssetRepository resource
   */
  public static void populateDevice(ApiDevice device, AssetRepository assetRepository) {
    List<ApiDevice> devices = Collections.singletonList(device);
    populateDevices(devices, assetRepository);
  }

  /**
   * Uses an AssetRepository to populate the "assets" list of each ApiDevice in "devices".
   * <br>
   * Generates 1 PostgreSQL query.
   *
   * @param devices         the list of devices to populate
   * @param assetRepository the AssetRepository resource
   */
  public static void populateDevices(List<ApiDevice> devices, AssetRepository assetRepository) {
    Map<UUID, ApiDevice> deviceMap = devices
        .stream()
        .collect(Collectors.toMap(ApiDevice::getId, Functions.identity()));
    List<UUID> deviceIds = new ArrayList<>(deviceMap.keySet());
    List<ApiAsset> assets = RepositoryUtils.toApiList(
        assetRepository.findAllByDeviceIdIn(deviceIds));

    populateDevices(deviceMap, assets);
    fillAssetCounts(devices, assetRepository);
  }

  public static void populateDevices(
      List<ApiDevice> devices,
      AssetRepository assetRepository,
      LogRepository logRepository) {
    populateDevices(devices, assetRepository);
    List<ApiAsset> assets = devices
        .stream()
        .flatMap(device -> device.getAssets().stream())
        .collect(Collectors.toList());
    populateAssetsLastLog(assets, logRepository);
  }

  public static void populateDevices(Map<UUID, ApiDevice> deviceMap, List<ApiAsset> assets) {
    for (ApiAsset asset : assets) {
      ApiDevice device = deviceMap.get(asset.getDeviceId());
      if (device != null) {
        device.addAsset(asset);
      }
    }
  }

  /**
   * Uses a DeviceRepository and an AssetRepository to populate the "assets" list and the "devices"
   * list of "location".
   * Each ApiDevice that is fetched for "location" will also be populated with its child ApiAssets.
   * <br>
   * Generates 2 PostgreSQL queries.
   *
   * @param location         the location to populate
   * @param deviceRepository the DeviceRepository resource
   * @param assetRepository  the AssetRepository resource
   */
  public static void populateLocation(
      ApiLocation location,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository) {
    List<ApiLocation> locations = Collections.singletonList(location);
    populateLocations(locations, deviceRepository, assetRepository);
  }

  /**
   * Uses a DeviceRepository and an AssetRepository to populate the "assets" list and the "devices"
   * list of each ApiLocation in "locations".
   * Each ApiDevice that is fetched for the ApiLocations in "locations" will also be populated with
   * their child ApiAssets.
   * <br>
   * Generates 2 PostgreSQL queries.
   *
   * @param locations        the list of locations to populate
   * @param deviceRepository the DeviceRepository resource
   * @param assetRepository  the AssetRepository resource
   */
  public static void populateLocations(
      List<ApiLocation> locations,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository) {
    // Get all devices for these locations
    Map<UUID, ApiLocation> locationMap = locations
        .stream()
        .collect(Collectors.toMap(ApiLocation::getId, Function.identity()));
    List<UUID> locationIds = new ArrayList<>(locationMap.keySet());

    // Get the devices at these locations
    List<ApiDevice> devices = RepositoryUtils.toApiList(
        deviceRepository.findAllByLocationIdIn(locationIds));

    // Get all assets for each location
    List<ApiAsset> assets = RepositoryUtils.toApiList(
        assetRepository.findAllByLocationIdIn(locationIds));

    // Populate the Location objects with all the fetched data
    populateDevices(devices, assetRepository);
    populateLocationsWithAssets(locationMap, assets);
    populateLocationsWithDevices(locationMap, devices);
  }

  public static void populateLocations(
      List<ApiLocation> locations,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository,
      LogRepository logRepository) {
    populateLocations(locations, deviceRepository, assetRepository);
    List<ApiAsset> assets = locations
        .stream()
        .flatMap(location -> location.getAssets().stream())
        .collect(Collectors.toList());
    populateAssetsLastLog(assets, logRepository);
  }

  public static void populateLocationsWithAssets(
      Map<UUID, ApiLocation> locationMap,
      List<ApiAsset> assets) {
    for (ApiAsset asset : assets) {
      ApiLocation location = locationMap.get(asset.getLocationId());
      if (location != null) {
        location.addAsset(asset);
      }
    }
  }

  public static void populateLocationsWithDevices(
      Map<UUID, ApiLocation> locationMap,
      List<ApiDevice> devices) {
    for (ApiDevice device : devices) {
      ApiLocation location = locationMap.get(device.getLocationId());
      if (location != null) {
        location.addDevice(device);
      }
    }
  }

  /**
   * Uses multiple *Repository resources to populate an ApiCompany with its children ApiLocation
   * objects. The ApiLocation objects that are fetched are further populated using
   * {@link #populateLocations(List, DeviceRepository, AssetRepository)}.
   * <br>
   * Generates 3 PostgreSQL queries.
   *
   * @param company            the company to populate
   * @param locationRepository the LocationRepository resource
   * @param deviceRepository   the DeviceRepository resource
   * @param assetRepository    the AssetRepository resource
   */
  public static void populateCompany(
      ApiCompany company,
      LocationRepository locationRepository,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository) {
    List<ApiLocation> locations = RepositoryUtils.toApiList(
        locationRepository.findAllByCompanyId(company.getId()));
    populateLocations(locations, deviceRepository, assetRepository);
    company.setLocations(locations);
  }

  public static void populateCompanies(
      List<ApiCompany> companies,
      LocationRepository locationRepository,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository) {
    for (ApiCompany company : companies) {
      populateCompany(company, locationRepository, deviceRepository, assetRepository);
    }
  }

  public static void populateNotification(
      ApiNotification notification,
      LocationRepository locationRepository,
      DeviceRepository deviceRepository,
      AssetRepository assetRepository,
      LogRepository logRepository) {
    populateLocations(notification.getLocations(), deviceRepository, assetRepository, logRepository);
    populateDevices(notification.getDevices(), assetRepository, logRepository);

    Set<UUID> locationIds = new HashSet<>(
        RepositoryUtils.mapToIds(RepositoryUtils.toDatabaseList(notification.getLocations())));
    for (ApiDevice device : notification.getDevices()) {
      locationIds.add(device.getLocationId());
    }

    for (ApiLocation location : notification.getLocations()) {
      if (locationIds.contains(location.getId())) {
        locationIds.remove(location.getId());
      }
    }

    if (!locationIds.isEmpty()) {
      List<UUID> locationIdsList = new ArrayList<>(locationIds);

      List<ApiLocation> locations = RepositoryUtils.toApiList(
          locationRepository.findAllByIdIn(new ArrayList<>(locationIdsList)));

      Map<UUID, ApiLocation> locationMap = locations
          .stream()
          .collect(Collectors.toMap(ApiLocation::getId, Functions.identity()));

      List<ApiAsset> assets = RepositoryUtils.toApiList(
          assetRepository.findAllByLocationIdIn(locationIdsList));

      populateLocationsWithDevices(locationMap, notification.getDevices());
      populateLocationsWithAssets(locationMap, assets);
      notification.getLocations().addAll(locations);
    }

    List<ApiDevice> allDevices = new ArrayList<>();
    for (ApiLocation location : notification.getLocations()) {
      allDevices.addAll(location.getDevices());
    }

    fillAssetCounts(allDevices, assetRepository);
  }

  public static void populateAssetsLastLog(List<ApiAsset> assets, LogRepository logRepository) {
    for (ApiAsset asset : assets) {
      Log lastLog = logRepository.findFirstByObjectIdOrderByTimestampDesc(asset.getId());
      if (lastLog != null) {
        asset.setLastLog(lastLog.toApi());
      }
    }
  }

  public static void fillAssetCounts(List<ApiDevice> devices, AssetRepository assetRepository) {
    for (ApiDevice device : devices) {
      fillAssetCounts(device, assetRepository);
    }
  }

  public static void fillAssetCounts(ApiDevice device, AssetRepository assetRepository) {
    int assetsInDevice = assetRepository.countAllByDeviceId(device.getId());
    int assetsWithHomeId = assetRepository.countAllByHomeId(device.getId());

    device.setAssetsInDevice(assetsInDevice);
    device.setAssetsWithHome(assetsWithHomeId);
  }

  public static List<ApiLocation> getLocationsForUser(
      ApiUser user,
      LocationRepository locationRepository) {
    if (AuthenticationUtils.userIsAtLeast(user, AccessLevel.ADMIN)) {
      return RepositoryUtils.toApiList(
          locationRepository.findAllByCompanyId(user.getCompanyId()));
    }

    return user.getLocations();
  }

  public static List<ApiAsset> filterByRfidIn(List<ApiAsset> assets, List<String> rfids) {
    return assets
        .stream()
        .filter((asset) -> rfids.contains(asset.getRfid()))
        .collect(Collectors.toList());
  }

  public static ApiCompany getCompanyForDevice(
      ApiDevice device,
      LocationRepository locationRepository,
      CompanyRepository companyRepository) {
    Location location = locationRepository.findOne(device.getLocationId());
    if (location != null) {
      Company company = companyRepository.findOne(location.getCompanyId());
      if (company != null) {
        return company.toApi();
      }
    }

    return null;
  }

  public static List<ApiAsset> generateAssetsForRfids(List<String> rfids, UUID homeId, ApiAssetType apiAssetType) {
    List<ApiAsset> assets = new ArrayList<>();

    for (String rfid : rfids) {
      ApiAsset newAsset = new ApiAsset();
      newAsset.setAssetType(apiAssetType);
      newAsset.setHomeId(homeId);
      newAsset.setDeviceId(homeId);
      newAsset.setName("{NEW ASSET}");
      newAsset.setRfid(rfid);
      newAsset.setSerial("");
      newAsset.setStatus(AssetStatus.AVAILABLE);

      assets.add(newAsset);
    }

    return assets;
  }

  public static ApiAssetType getOrCreateDefaultAssetType(
      UUID companyId,
      AssetTypeRepository assetTypeRepository) {
    String defaultAssetTypeName = ApiAssetType.DEFAULT_ASSET_TYPE;

    AssetType assetType = assetTypeRepository
        .findOneByCompanyIdAndType(companyId, defaultAssetTypeName);

    if (assetType == null) {
      // create default asset type
      assetType = new AssetType();
      assetType.setCompanyId(companyId);
      assetType.setType(defaultAssetTypeName);
      assetTypeRepository.saveAndFlush(assetType);
    }

    return assetType.toApi();
  }

  public static void populateLogUserFullNames(List<ApiLog> logs, UserRepository userRepository) {
    Set<UUID> userUuidSet = new HashSet<>();
    for (ApiLog log : logs) {
      if (UUIDUtils.isValidId(log.getUserId())) {
        userUuidSet.add(log.getUserId());
      }
    }

    ArrayList<UUID> userUuidList = new ArrayList<>(userUuidSet);
    List<User> users = userRepository.findAllByIdIn(userUuidList);
    Map<UUID, User> idToUserMap = users.stream()
        .collect(Collectors.toMap(User::getId, Function.identity()));

    for (ApiLog log : logs) {
      if (UUIDUtils.isValidId(log.getUserId())) {
        User user = idToUserMap.get(log.getUserId());
        log.setUserFullName(user.getFirstName() + ' ' + user.getLastName());
      }
    }
  }
}
