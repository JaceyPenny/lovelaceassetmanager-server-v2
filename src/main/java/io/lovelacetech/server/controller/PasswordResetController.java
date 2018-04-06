package io.lovelacetech.server.controller;

import io.lovelacetech.server.model.PasswordReset;
import io.lovelacetech.server.model.api.model.ApiPasswordReset;
import io.lovelacetech.server.model.api.response.DefaultApiResponse;
import io.lovelacetech.server.model.api.response.passwordreset.PasswordResetApiResponse;
import io.lovelacetech.server.repository.PasswordResetRepository;
import io.lovelacetech.server.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/passwordReset")
public class PasswordResetController {

  private PasswordResetService passwordResetService;
  private PasswordResetRepository passwordResetRepository;

  @Autowired
  public PasswordResetController(
      PasswordResetService passwordResetService,
      PasswordResetRepository passwordResetRepository) {
    this.passwordResetService = passwordResetService;
    this.passwordResetRepository = passwordResetRepository;
  }

  /**
   * <b>  GET /api/passwordReset/request/{email}  </b>
   * <br><br>
   * Requests a password reset by email. An error will be thrown if:<br>
   * <ul>
   *   <li>The email format is invalid</li>
   *   <li>The email does not correspond to any user</li>
   *   <li>The user has already requested a reset within the past 24 hours.</li>
   * </ul><br>
   * Otherwise, the user's request will be granted, and they will receive an email with the reset
   * code.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code {
   *   "status": 200,
   *   "message": "success",
   *   "response": null
   * }}</pre>
   */
  @GetMapping(value = "/request/{email:.+}")
  public PasswordResetApiResponse requestResetByEmail(@PathVariable(name = "email") String email) {
    if (passwordResetService.requestReset(email) == null) {
      return new PasswordResetApiResponse().setNotFound();
    } else {
      return new PasswordResetApiResponse().setSuccess();
    }
  }

  /**
   * <b>  GET /api/passwordReset/{code}  </b>
   * <br><br>
   * Gets the password reset information corresponding to the request code. This endpoint is not
   * particularly useful, and is provided for debugging purposes.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code {
   *   "status": 200,
   *   "message": "success",
   *   "response": {
   *     "id": UUID
   *     "user": User (sanitized)
   *     "code": String
   *   }
   * }}</pre>
   */
  @GetMapping(value = "/{code}")
  public PasswordResetApiResponse getByCode(@PathVariable String code) {
    PasswordReset passwordReset = passwordResetRepository.findByCode(code);
    if (passwordReset == null) {
      return new PasswordResetApiResponse().setNotFound();
    } else {
      ApiPasswordReset response = passwordReset.toApi();
      response.getUser().sanitize();
      return new PasswordResetApiResponse().setSuccess().setResponse(response);
    }
  }

  /**
   * <b>  POST /api/passwordReset/  </b>
   * <br><br>
   * Finalizes and resets the user's password to the desired password in "newPassword". The new
   * password must meet the same criteria as for registering a new user.
   * <br><br>
   * <b>  REQUEST BODY:</b><br>
   * <pre>{@code {
   *   "code": String (password reset code provided in email),
   *   "newPassword": String (the desired new password)
   * }}</pre>
   * <br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code {
   *   "status": 200,
   *   "message": "success",
   *   "response": null
   * }}</pre>
   */
  @PostMapping(value = "/")
  public DefaultApiResponse resetPassword(@RequestBody Map<String, String> body) {
    String resetCode = body.get("code");
    String newPassword = body.get("newPassword");

    if (resetCode == null || newPassword == null) {
      return new DefaultApiResponse().setInvalidBody();
    }

    if (passwordResetService.resetPassword(resetCode, newPassword)) {
      return new DefaultApiResponse().setSuccess();
    } else {
      return new DefaultApiResponse().setNotFound();
    }
  }
}
