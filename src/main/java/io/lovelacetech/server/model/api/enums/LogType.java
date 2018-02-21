package io.lovelacetech.server.model.api.enums;

import java.util.HashMap;

public enum LogType {
  ASSET_REGISTERED(0),
  ASSET_EDITED(1),
  ASSET_MOVED(2),
  ASSET_DELETED(3),
  DEVICE_REGISTERED(4),
  DEVICE_EDITED(5),
  DEVICE_DELETED(6),
  LOCATION_ADDED(7),
  LOCATION_EDITED(8),
  LOCATION_DELETED(9),
  USER_REGISTERED(10),
  USER_EDITED(11),
  USER_DELETED(12),
  REPORT_GENERATED(13);


  private int type;
  LogType(int type) {
    this.type = type;
  }

  private static HashMap<Integer, LogType> valueToEnumMap;

  public static LogType fromInt(int value) {
    if (valueToEnumMap == null) {
      valueToEnumMap = new HashMap<>();
      for (LogType logType : LogType.values()) {
        valueToEnumMap.put(logType.toInt(), logType);
      }
    }

    return valueToEnumMap.get(value);
  }

  public int toInt() {
    return this.type;
  }

  public String toString() {
    return String.valueOf(this.type);
  }
}
