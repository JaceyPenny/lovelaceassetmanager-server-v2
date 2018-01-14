package io.lovelacetech.server.util;

import com.google.common.base.Functions;
import io.lovelacetech.server.model.api.model.ApiAsset;
import io.lovelacetech.server.model.api.model.ApiCompany;
import io.lovelacetech.server.model.api.model.ApiDevice;
import io.lovelacetech.server.model.api.model.ApiLocation;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.DeviceRepository;
import io.lovelacetech.server.repository.LocationRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LoaderUtils {
  /**
   * Uses an AssetRepository to populate the "assets" list of "device".
   * <br>
   * Generates 1 PostgreSQL query.
   *
   * @param device the device to populate
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
   * @param devices the list of devices to populate
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
   * @param location the location to populate
   * @param deviceRepository the DeviceRepository resource
   * @param assetRepository the AssetRepository resource
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
   * @param locations the list of locations to populate
   * @param deviceRepository the DeviceRepository resource
   * @param assetRepository the AssetRepository resource
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
   * @param company the company to populate
   * @param locationRepository the LocationRepository resource
   * @param deviceRepository the DeviceRepository resource
   * @param assetRepository the AssetRepository resource
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
}
