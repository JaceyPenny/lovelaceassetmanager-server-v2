package io.lovelacetech.server.util;

import com.google.common.collect.Streams;
import io.lovelacetech.server.model.ApiModelConvertible;
import io.lovelacetech.server.model.DatabaseModel;
import io.lovelacetech.server.model.api.model.BaseApiModel;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RepositoryUtils {
  public static <R extends ApiModelConvertible<T>, T extends BaseApiModel<R>> List<T> toApiList(
      Iterable<? extends ApiModelConvertible<T>> iterable) {
    return Streams.stream(iterable)
        .map(ApiModelConvertible::toApi)
        .collect(Collectors.toList());
  }

  public static <R extends ApiModelConvertible<T>, T extends BaseApiModel<R>>
      List<R> toDatabaseList(Iterable<T> iterable) {
    return Streams.stream(iterable)
        .map(BaseApiModel::toDatabase)
        .collect(Collectors.toList());
  }

  public static <FROM, TO> List<TO> mapList(
      List<FROM> list,
      Function<FROM, ? extends TO> function) {
    return list.stream().map(function).collect(Collectors.toList());
  }

  public static <T extends DatabaseModel> boolean updateConflictsWithExistingRow(
      T update,
      T existingRow) {
    return !update.hasId() || !existingRow.idEquals(update.getId());
  }
}
