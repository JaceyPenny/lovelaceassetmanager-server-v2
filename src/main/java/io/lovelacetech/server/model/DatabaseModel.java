package io.lovelacetech.server.model;

import java.util.UUID;

public interface DatabaseModel<T extends DatabaseModel> {
  UUID getId();
  boolean hasId();
  boolean idEquals(UUID otherId);
  void applyUpdate(T other);
}
