package io.lovelacetech.server.model.api.response.log;

import io.lovelacetech.server.model.Log;
import io.lovelacetech.server.model.api.model.ApiLogList;
import io.lovelacetech.server.model.api.response.BaseApiResponse;

public class LogListApiResponse extends BaseApiResponse<LogListApiResponse, ApiLogList> {
  public LogListApiResponse setResponse(Iterable<Log> logs) {
    return super.setResponse(new ApiLogList(logs));
  }
}
