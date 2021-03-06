package io.lovelacetech.server.command;

import io.lovelacetech.server.model.api.response.BaseApiResponse;

public interface BaseCommand {
  boolean checkCommand();

  <T extends BaseApiResponse<?, ?>> T execute();
}
