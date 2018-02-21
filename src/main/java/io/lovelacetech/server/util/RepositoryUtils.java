package io.lovelacetech.server.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Streams;
import io.lovelacetech.server.model.ApiModelConvertible;
import io.lovelacetech.server.model.DatabaseModel;
import io.lovelacetech.server.model.api.model.BaseApiModel;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RepositoryUtils {
  public static <R extends ApiModelConvertible<T>, T extends BaseApiModel<R>> List<T> toApiList(
      Iterable<? extends ApiModelConvertible<T>> iterable) {
    return mapIterable(iterable, ApiModelConvertible::toApi);
  }

  public static <R extends ApiModelConvertible<T>, T extends BaseApiModel<R>>
  List<R> toDatabaseList(Iterable<T> iterable) {
    return mapIterable(iterable, BaseApiModel::toDatabase);
  }

  public static <FROM, TO> List<TO> mapIterable(
      Iterable<FROM> iterable,
      Function<FROM, ? extends TO> function) {
    return iterable == null ? new ArrayList<>() : Streams.stream(iterable).map(function).collect(Collectors.toList());
  }

  public static <T extends DatabaseModel> boolean updateConflictsWithExistingRow(
      T update,
      T existingRow) {
    return !update.hasId() || !existingRow.idEquals(update.getId());
  }

  public static <T extends DatabaseModel> boolean listContainsRow(List<T> list, T row) {
    for (T item : list) {
      if (UUIDUtils.idsEqual(item.getId(), row.getId())) {
        return true;
      }
    }

    return false;
  }

  public static <T extends DatabaseModel> List<UUID> mapToIds(List<T> list) {
    return list.stream().map(T::getId).collect(Collectors.toList());
  }

  public static Map<String, Object> jsonStringToMap(String json) {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map = new HashMap<>();

    try {
      map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});
    } catch (IOException e) {
      // do nothing
    }

    return map;
  }

  public static String mapToJsonString(Map<String, Object> map) {
    ObjectMapper mapper = new ObjectMapper();
    String json = "";

    try {
      json = mapper.writeValueAsString(map);
    } catch (IOException e) {
      // do nothing
    }

    return json;
  }
}
