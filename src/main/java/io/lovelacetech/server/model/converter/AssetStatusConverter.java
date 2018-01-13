package io.lovelacetech.server.model.converter;

import io.lovelacetech.server.model.api.enums.AssetStatus;

import javax.persistence.AttributeConverter;

public class AssetStatusConverter implements AttributeConverter<AssetStatus, String> {
  @Override
  public String convertToDatabaseColumn(AssetStatus attribute) {
    return attribute != null ? attribute.toString() : null;
  }

  @Override
  public AssetStatus convertToEntityAttribute(String dbData) {
    return dbData != null ? AssetStatus.fromString(dbData) : null;
  }
}
