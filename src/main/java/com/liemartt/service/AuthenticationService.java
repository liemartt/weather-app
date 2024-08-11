package com.liemartt.service;

import com.liemartt.dao.SessionDAO;
import com.liemartt.dao.SessionDAOImpl;
import com.liemartt.entity.Session;
import com.liemartt.entity.User;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

public class AuthenticationService {
    @Getter
    private final static AuthenticationService INSTANCE = new AuthenticationService();
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    
    private AuthenticationService() {
    }
    
    
    public Optional<User> getAuthorizedUser(String sessionId) {
        try {
            UUID sessionUUID = UUID.fromString(sessionId);
            Optional<Session> session = sessionDAO.getSessionByUUID(sessionUUID);
            return session.map(Session::getUser);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
    
    
    public boolean isSessionValid(String sessionId) {
        try {
            UUID sessionUUID = UUID.fromString(sessionId);
            Optional<Session> session = sessionDAO.getSessionByUUID(sessionUUID);
            return session.isPresent();
        } catch (IllegalArgumentException e) {
            return false;
        }
        
    }
    
}

