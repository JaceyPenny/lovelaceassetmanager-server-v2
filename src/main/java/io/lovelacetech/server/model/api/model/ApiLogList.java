package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.Log;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.ArrayList;
import java.util.List;

public class ApiLogList extends BaseApiModel {
  List<ApiLog> logs;

  public ApiLogList() {
    this.logs = new ArrayList<>();
  }

  public ApiLogList(Iterable<Log> logs) {
    this.logs = RepositoryUtils.toApiList(logs);
  }

  public ApiLogList(List<ApiLog> logs) {
    this.logs = logs;
  }

  public List<ApiLog> getLogs() {
    return logs;
  }

  public ApiLogList setLogs(List<ApiLog> logs) {
    this.logs = logs;
    return this;
  }

  public ApiLogList addLog(ApiLog log) {
    logs.add(log);
    return this;
  }
}
