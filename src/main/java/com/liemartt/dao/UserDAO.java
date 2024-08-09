package com.liemartt.dao;

import com.liemartt.entity.User;
import com.liemartt.exception.UserNotExistsException;
import com.liemartt.exception.UsernameAlreadyExistsException;

import java.util.Optional;

public interface UserDAO {
    void saveUser(User user) throws UsernameAlreadyExistsException;
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserById(Long id) throws UserNotExistsException;
}
