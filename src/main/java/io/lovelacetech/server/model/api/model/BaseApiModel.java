package io.lovelacetech.server.model.api.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lovelacetech.server.model.ApiModelConvertible;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;

public class BaseApiModel<T extends ApiModelConvertible<? extends BaseApiModel>> {
  @Override
  public String toString() {
    Gson gson = new GsonBuilder().create();
    return gson.toJson(this);
  }

  @Transient
  public boolean isValid() {
    return true;
  }

  public T toDatabase() {
    return null;
  }

  public Map<String, Object> toLogObject() {
    return new HashMap<>();
  }
}
