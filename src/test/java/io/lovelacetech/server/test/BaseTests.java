package io.lovelacetech.server.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseTests {
  public static String jsonString(Object value) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(value);
  }
}
