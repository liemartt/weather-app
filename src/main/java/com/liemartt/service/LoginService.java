package com.liemartt.service;

import com.liemartt.dao.UserDAO;
import com.liemartt.dao.UserDAOImpl;
import com.liemartt.dto.UserDto;
import com.liemartt.entity.User;
import com.liemartt.exception.IncorrectPasswordException;
import com.liemartt.exception.UserNotFoundException;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class LoginService {
    @Getter
    private final static LoginService INSTANCE = new LoginService();
    private final UserDAO userDAO = new UserDAOImpl();
    
    private LoginService() {
    }
    
    public User validateUserCredentials(UserDto userDto) throws UserNotFoundException, IncorrectPasswordException {
        Optional<User> userOptional = userDAO.getUserByUsername(userDto.getUsername());
        if (!BCrypt.checkpw(userDto.getPassword(), userOptional.get().getPassword())) {
            throw new IncorrectPasswordException();
        }
        return userOptional.get();
    }
    
    
}
