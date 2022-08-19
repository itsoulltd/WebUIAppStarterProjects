package com.infoworks.lab.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/greetings")
public class GreetingController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String greetings(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
            , @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String lang) {
        //
        Locale locale = Locale.forLanguageTag(lang);
        return messageSource.getMessage("app.greetings", null, locale);
    }
}
