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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class LoginService {
    @Getter
    private final static LoginService INSTANCE = new LoginService();
    private final UserDAO userDAO = new UserDAOImpl();
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    private static final int SESSION_LIFETIME_MINUTES = 30;
    
    private LoginService() {
    }
    
    public Session loginUser(UserDto userDto) throws UserNotFoundException, IncorrectPasswordException {
        Optional<User> userOptional = userDAO.findByUsername(userDto.getUsername());
        if (!BCrypt.checkpw(userDto.getPassword(), userOptional.get().getPassword())) {
            throw new IncorrectPasswordException();
        }
        
        Session session = new Session(UUID.randomUUID(), userOptional.get(), LocalDateTime.now().plusMinutes(SESSION_LIFETIME_MINUTES));
        sessionDAO.save(session);
        
        return session;
    }
    
    
}
