package io.lovelacetech.server.command;

import io.lovelacetech.server.model.api.response.BaseApiResponse;

public interface Responds<T extends BaseApiResponse> {
  T execute();
}
