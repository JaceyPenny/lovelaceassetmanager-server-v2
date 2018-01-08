package io.lovelacetech.server.util;

import java.util.UUID;

public class UUIDUtils {

  public static UUID empty() {
    return new UUID(0, 0);
  }

  public static boolean isValidId(UUID id) {
    return id != null && !id.equals(UUIDUtils.empty());
  }

  public static boolean idsEqual(UUID id, UUID otherId) {
    return id == otherId || (id != null && id.equals(otherId));
  }

}
