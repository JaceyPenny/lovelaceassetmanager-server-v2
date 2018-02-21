package io.lovelacetech.server.model.api.enums;

import java.util.HashMap;

public enum LogType {
  ASSET_REGISTERED(0),
  ASSET_EDITED(1),
  ASSET_MOVED(2),
  ASSET_REMOVED(3),
  REPORT_GENERATED(4);


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
