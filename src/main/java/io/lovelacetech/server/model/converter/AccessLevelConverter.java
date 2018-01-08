package io.lovelacetech.server.model.converter;

import io.lovelacetech.server.model.api.enums.AccessLevel;

import javax.persistence.AttributeConverter;

public class AccessLevelConverter implements AttributeConverter<AccessLevel, Integer> {
  @Override
  public Integer convertToDatabaseColumn(AccessLevel attribute) {
    return attribute.toInt();
  }

  @Override
  public AccessLevel convertToEntityAttribute(Integer dbData) {
    return AccessLevel.fromInt(dbData);
  }
}
