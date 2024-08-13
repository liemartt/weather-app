package com.liemartt.dao;

import com.liemartt.entity.User;
import com.liemartt.exception.UserNotFoundException;
import com.liemartt.exception.UsernameExistsException;

import java.util.Optional;

public interface UserDAO {
    void save(User user) throws UsernameExistsException;
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id) throws UserNotFoundException;
}
