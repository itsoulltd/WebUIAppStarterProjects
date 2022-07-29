package com.infoworks.lab.controllers.rest;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @GetMapping("/hello")
    public String hello() {
        return "Full Stack Java with Spring Boot & VueJS-2!";
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, Object> jsonData) {
        return "Login Successful with VueJS-2! Username: " + jsonData.get("username");
    }

}
