package com.jdev.projects.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jdev.projects.model.User;
import com.jdev.projects.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.signUp(user);
        return new ResponseEntity<>("User has been registerd", HttpStatus.CREATED);
    }
    
}
