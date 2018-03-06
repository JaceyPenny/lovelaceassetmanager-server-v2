package io.lovelacetech.server.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
  public static <T> List<T> subtract(List<T> a, List<T> b) {
    List<T> difference = new ArrayList<>(a);

    for (T value : b) {
      difference.remove(value);
    }

    return difference;
  }
}
