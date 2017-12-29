package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.model.BaseApiModel;

public interface ApiModelConvertible<T extends BaseApiModel> {
  T toApi();
}
