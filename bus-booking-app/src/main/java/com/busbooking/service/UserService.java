package com.busbooking.service;

import com.busbooking.entity.User;

public interface UserService {

    User registerUser(User user);

    String updateUser(User user);
}
