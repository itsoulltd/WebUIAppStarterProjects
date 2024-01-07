package com.infoworks.lab.controllers.rest;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Locale;

@Controller
public class HomeController {

    private MessageSource messageSource;

    public HomeController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/index")
    public String greetings(Model model
            , @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, required = false) String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        String greeting = messageSource.getMessage("app.greetings", null, locale);
        model.addAttribute("greeting", greeting);
        return "index";
    }
}
