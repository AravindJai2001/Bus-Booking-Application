package com.busbooking.controller;

import com.busbooking.dto.LoginRequest;
import com.busbooking.entity.User;
import com.busbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody  User user){

        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user){
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }


}
