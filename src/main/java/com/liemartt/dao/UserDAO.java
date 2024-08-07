package com.liemartt.dao;

import com.liemartt.entity.User;

import java.util.Optional;

public interface UserDAO {
    void saveUser(User user);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserById(Long id);
}
