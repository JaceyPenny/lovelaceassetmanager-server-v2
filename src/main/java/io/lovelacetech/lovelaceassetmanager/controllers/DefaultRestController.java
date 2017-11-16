package io.lovelacetech.lovelaceassetmanager.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class DefaultRestController {
    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String defaultGet() {
        return "Lovelace Technologies API v2. Successful GET.";
    }
}
