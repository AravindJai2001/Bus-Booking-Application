package com.busbooking.service;

import com.busbooking.entity.User;
import com.busbooking.repository.UserRepository;
import com.busbooking.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    @Cacheable(value = "userCache", key = "#emailId")
    public UserDetails loadUserByUsername(String emailId) {
        User user = userRepo.findByEmail(emailId).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Loading user from DB: " + emailId);
        return new UserPrincipal(user);
    }
}
