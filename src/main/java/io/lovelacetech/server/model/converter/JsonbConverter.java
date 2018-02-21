package io.lovelacetech.server.model.converter;

import io.lovelacetech.server.util.RepositoryUtils;

import javax.persistence.AttributeConverter;
import java.util.Map;

public class JsonbConverter implements AttributeConverter<Map<String, Object>, String> {
  @Override
  public String convertToDatabaseColumn(Map<String, Object> attribute) {
    return attribute != null ? RepositoryUtils.mapToJsonString(attribute) : null;
  }

  @Override
  public Map<String, Object> convertToEntityAttribute(String dbData) {
    return dbData != null ? RepositoryUtils.jsonStringToMap(dbData) : null;
  }
}
