package com.liemartt.dao.user;

import com.liemartt.entity.User;
import com.liemartt.exception.user.UsernameExistsException;

import java.util.Optional;

public interface UserDAO {
    void save(User user) throws UsernameExistsException;
    Optional<User> findByUsername(String username);
}
