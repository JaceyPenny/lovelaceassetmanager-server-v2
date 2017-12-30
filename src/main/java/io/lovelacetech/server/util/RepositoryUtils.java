package io.lovelacetech.server.util;

import com.google.common.collect.Streams;
import io.lovelacetech.server.model.ApiModelConvertible;
import io.lovelacetech.server.model.api.model.BaseApiModel;

import java.util.List;
import java.util.stream.Collectors;

public class RepositoryUtils {
  public static <T extends BaseApiModel> List<T> toApiList(
      Iterable<? extends ApiModelConvertible<T>> iterable) {
    return Streams.stream(iterable)
        .map(ApiModelConvertible::toApi)
        .collect(Collectors.toList());
  }
}