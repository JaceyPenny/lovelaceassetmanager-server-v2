package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.log.LogListApiResponse;
import io.lovelacetech.server.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/secure/logs")
public class LogController extends BaseController{
  private final LogRepository logRepository;

  @Autowired
  public LogController(LogRepository logRepository) {
    this.logRepository = logRepository;
  }

  /**
   * /**
   * <b>  GET /api/secure/logs/</b><br><br>
   * Gets all the Log objects in the database.
   * <br><br><b>  RESPONSE:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "logs": [Log]
   *   }
   * }}</pre>
   * <br><br><b>  PERMISSIONS:  </b><br>
   * This method is only accessible to SUPER users.
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public LogListApiResponse getLogs(@RequestAttribute ApiUser authenticatedUser) {
    checkIsSuper(authenticatedUser);

    return new LogListApiResponse()
        .setSuccess()
        .setResponse(logRepository.findAll());
  }
}
