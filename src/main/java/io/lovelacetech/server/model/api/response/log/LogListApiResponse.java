package io.lovelacetech.server.model.api.response.log;

import io.lovelacetech.server.model.Log;
import io.lovelacetech.server.model.api.model.ApiLog;
import io.lovelacetech.server.model.api.model.ApiLogList;
import io.lovelacetech.server.model.api.response.BaseApiResponse;

import java.util.List;

public class LogListApiResponse extends BaseApiResponse<LogListApiResponse, ApiLogList> {
  public LogListApiResponse setResponse(Iterable<Log> logs) {
    return super.setResponse(new ApiLogList(logs));
  }

  public LogListApiResponse setResponse(List<ApiLog> logs) {
    return super.setResponse(new ApiLogList(logs));
  }
}
