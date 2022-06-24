package com.service.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {

    @GetMapping("/morning")
    public String goodMorning(){
        return "Very Good Morning";
    }
}
