package com.liemartt.service;

import com.liemartt.dao.SessionDAO;
import com.liemartt.dao.SessionDAOImpl;
import com.liemartt.dao.UserDAO;
import com.liemartt.dao.UserDAOImpl;
import com.liemartt.dto.UserDto;
import com.liemartt.entity.Session;
import com.liemartt.entity.User;
import com.liemartt.exception.IncorrectPasswordException;
import com.liemartt.exception.UserNotFoundException;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.UUID;

public class LoginService {
    @Getter
    private final static LoginService INSTANCE = new LoginService();
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();
    
    private LoginService() {
    }
    
    public User validateUser(UserDto userDto) throws UserNotFoundException, IncorrectPasswordException {
        Optional<User> userOptional = userDAO.getUserByUsername(userDto.getUsername());
        if (!BCrypt.checkpw(userDto.getPassword(), userOptional.get().getPassword())) {
            throw new IncorrectPasswordException();
        }
        return userOptional.get();
    }
    
    
    public boolean isSessionValid(UUID sessionUUID) {//todo move in SessionService?
        Optional<Session> session = sessionDAO.getSessionByUUID(sessionUUID);
        return session.isPresent();
    }
}
