package io.lovelacetech.server.model.api.enums;

import java.util.HashMap;

public enum AccessLevel {
  USER(0),
  ADMIN(1),
  SUPER(2);

  private int value;
  AccessLevel(int value) {
    this.value = value;
  }

  private static HashMap<Integer, AccessLevel> valueToEnumMap;
  public static AccessLevel fromInt(int value) {
    if (valueToEnumMap == null) {
      valueToEnumMap = new HashMap<>();
      for (AccessLevel accessLevel : AccessLevel.values()) {
        valueToEnumMap.put(accessLevel.toInt(), accessLevel);
      }
    }

    return valueToEnumMap.get(value);
  }

  public int toInt() {
    return this.value;
  }

  @Override
  public String toString() {
    return String.valueOf(this.value);
  }
}