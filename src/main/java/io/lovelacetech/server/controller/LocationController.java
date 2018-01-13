package io.lovelacetech.server.controller;

import io.lovelacetech.server.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secure/locations")
public class LocationController {

  @Autowired
  LocationRepository locationRepository;

//  @RequestMapping(value = "/", method = RequestMethod.GET)
}
