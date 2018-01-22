package io.lovelacetech.server.model.converter;

import io.lovelacetech.server.model.api.enums.NotificationType;

import javax.persistence.AttributeConverter;

public class NotificationTypeConverter implements AttributeConverter<NotificationType, String> {
  @Override
  public String convertToDatabaseColumn(NotificationType attribute) {
    return attribute != null ? attribute.toString() : null;
  }

  @Override
  public NotificationType convertToEntityAttribute(String dbData) {
    return dbData != null ? NotificationType.fromString(dbData) : null;
  }
}
