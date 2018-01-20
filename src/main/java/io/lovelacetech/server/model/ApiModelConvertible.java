package io.lovelacetech.server.model;

import io.lovelacetech.server.model.api.model.BaseApiModel;

import java.util.UUID;

public interface ApiModelConvertible<T extends BaseApiModel<? extends ApiModelConvertible>> {
  T toApi();
}
