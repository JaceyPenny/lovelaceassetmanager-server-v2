package io.lovelacetech.server.model.api.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lovelacetech.server.model.ApiModelConvertible;

public class BaseApiModel {
  @Override
  public String toString() {
    Gson gson = new GsonBuilder().create();
    return gson.toJson(this);
  }

  public <T extends ApiModelConvertible> T toDatabase() {
    return null;
  }
}
