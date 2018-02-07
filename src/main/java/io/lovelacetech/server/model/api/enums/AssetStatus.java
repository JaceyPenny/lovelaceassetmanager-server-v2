package io.lovelacetech.server.model.api.enums;

import java.util.HashMap;

public enum AssetStatus {
  AVAILABLE("available"),
  REPAIR("repair");

  private String value;

  AssetStatus(String value) {
    this.value = value;
  }

  private static HashMap<String, AssetStatus> valueToEnumMap;

  public static AssetStatus fromString(String value) {
    if (valueToEnumMap == null) {
      valueToEnumMap = new HashMap<>();
      for (AssetStatus assetStatus : AssetStatus.values()) {
        valueToEnumMap.put(assetStatus.value, assetStatus);
      }
    }

    return valueToEnumMap.get(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
