package io.lovelacetech.server.model.converter;

import io.lovelacetech.server.model.api.enums.LogType;

import javax.persistence.AttributeConverter;

public class LogTypeConverter implements AttributeConverter<LogType, Integer> {
  @Override
  public Integer convertToDatabaseColumn(LogType attribute) {
    return attribute != null ? attribute.toInt() : null;
  }

  @Override
  public LogType convertToEntityAttribute(Integer dbData) {
    return dbData != null ? LogType.fromInt(dbData) : null;
  }
}
