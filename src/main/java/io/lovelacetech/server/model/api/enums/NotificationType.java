package io.lovelacetech.server.model.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;

public enum NotificationType {
  EMAIL("email"),
  TEXT("text"),
  EMAIL_TEXT("email_text");

  private String value;

  NotificationType(String value) {
    this.value = value;
  }

  private static HashMap<String, NotificationType> valueToEnumMap;

  @JsonCreator
  public static NotificationType fromString(String value) {
    if (valueToEnumMap == null) {
      valueToEnumMap = new HashMap<>();
      for (NotificationType notificationType : NotificationType.values()) {
        valueToEnumMap.put(notificationType.value, notificationType);
      }
    }

    return valueToEnumMap.get(value);
  }

  @JsonValue
  @Override
  public String toString() {
    return value;
  }
}
