package io.lovelacetech.server.util;

import com.google.common.collect.Streams;
import io.lovelacetech.server.model.ApiModelConvertible;
import io.lovelacetech.server.model.DatabaseModel;
import io.lovelacetech.server.model.api.model.BaseApiModel;

import java.util.ArrayList;
import java.util.List;
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
}
