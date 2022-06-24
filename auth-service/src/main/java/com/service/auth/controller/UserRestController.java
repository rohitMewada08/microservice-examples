package com.service.auth.controller;

import com.service.auth.dto.User;
import com.service.auth.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    UserDataService userDataService;

    @PostMapping("/save")
    public String addUser(@RequestBody User user){
        return userDataService.save(user);
    }

    @GetMapping("/get/{userId}")
    public User addUser(@PathVariable Long userId){
        return userDataService.get(userId);
    }

}
