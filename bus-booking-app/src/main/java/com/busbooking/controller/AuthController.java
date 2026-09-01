package com.busbooking.controller;

import com.busbooking.dto.LoginRequest;
import com.busbooking.entity.User;
import com.busbooking.repository.UserRepository;
import com.busbooking.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository repo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){
        User user = repo.findByEmail(request.emailId()).orElseThrow(() -> new RuntimeException("User not found"));

        if(!encoder.matches(request.password(), user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        System.out.println(token);
        return token;
    }
}
