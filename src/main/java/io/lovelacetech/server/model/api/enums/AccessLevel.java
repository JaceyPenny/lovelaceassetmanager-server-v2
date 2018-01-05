package io.lovelacetech.server.model.api.enums;

import java.util.HashMap;

public enum AccessLevel {
  DEFAULT(0),
  USER(1),
  ADMIN(2),
  SUPER(3);

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