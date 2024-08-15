package com.liemartt.service;

import com.liemartt.dao.session.SessionDAO;
import com.liemartt.dao.session.SessionDAOImpl;
import com.liemartt.dao.user.UserDAO;
import com.liemartt.dao.user.UserDAOImpl;
import com.liemartt.dto.UserDto;
import com.liemartt.entity.Session;
import com.liemartt.entity.User;
import com.liemartt.exception.user.IncorrectPasswordException;
import com.liemartt.exception.user.UserNotFoundException;
import com.liemartt.exception.user.UsernameExistsException;
import lombok.Getter;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class AuthenticationService {
    @Getter
    private final static AuthenticationService INSTANCE = new AuthenticationService();
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();
    private static final int SESSION_LIFETIME_MINUTES = 30;
    
    private AuthenticationService() {
    }
    
    
    public Optional<User> getAuthorizedUser(String sessionId) {
        try {
            UUID sessionUUID = UUID.fromString(sessionId);
            Optional<Session> session = sessionDAO.findById(sessionUUID);
            return session.map(Session::getUser);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
    
    
    public boolean isSessionActive(String sessionId) {
        try {
            UUID sessionUUID = UUID.fromString(sessionId);
            Optional<Session> session = sessionDAO.findById(sessionUUID);
            return session.isPresent();
        } catch (IllegalArgumentException e) {
            return false;
        }
        
    }
    
    public Session loginUser(UserDto userDto) throws UserNotFoundException, IncorrectPasswordException {
        User user = userDAO
                .findByUsername(userDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Not found"));
        
        if (!BCrypt.checkpw(userDto.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Not found");
        }
        
        Session session = new Session(UUID.randomUUID(), user, LocalDateTime.now().plusMinutes(SESSION_LIFETIME_MINUTES));
        sessionDAO.save(session);
        
        return session;
    }
    
    public void signupNewUser(UserDto userDto) throws UsernameExistsException {
        String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
        User user = new User(userDto.getUsername(), hashedPassword);
        userDAO.save(user);
    }
    
}

