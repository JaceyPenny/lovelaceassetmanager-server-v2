package io.lovelacetech.server.controller;

import io.lovelacetech.server.util.Messages;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The landing API controller. Doesn't do anything
 */
@RestController
@CrossOrigin
@RequestMapping("/")
public class DefaultController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String get() {
    return Messages.API_WELCOME;
  }
}
