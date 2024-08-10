package com.liemartt.service;

import com.liemartt.dao.SessionDAO;
import com.liemartt.dao.SessionDAOImpl;
import com.liemartt.entity.Session;
import com.liemartt.entity.User;
import jakarta.servlet.http.Cookie;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class AuthenticationService {
    @Getter
    private final static AuthenticationService INSTANCE = new AuthenticationService();
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    
    private AuthenticationService() {
    }
    
    
    public Optional<User> getAuthorizedUser(String sessionId) {
        UUID sessionUUID = UUID.fromString(sessionId);
        Optional<Session> session = sessionDAO.getSessionByUUID(sessionUUID);
        return session.map(Session::getUser);
    }
    
    
    public boolean isSessionValid(String sessionId){
        UUID sessionUUID = UUID.fromString(sessionId);
        Optional<Session> session = sessionDAO.getSessionByUUID(sessionUUID);
        return session.isPresent();
    }
    
}

