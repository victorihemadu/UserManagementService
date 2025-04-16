package com.UserManagement.services;

import com.UserManagement.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);

    User findByUsername(String username);
}

