package com.liemartt.service;

import com.liemartt.dao.SessionDAO;
import com.liemartt.dao.SessionDAOImpl;
import jakarta.servlet.http.Cookie;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class LogoutService {
    @Getter
    private final static LogoutService INSTANCE = new LogoutService();
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    
    public LogoutService() {
    }
    
    public void logout(Cookie[] cookies){
        Optional<Cookie> sessionCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("sessionId"))
                .findFirst();
        if (sessionCookie.isPresent()) {
            UUID sessionId = UUID.fromString(sessionCookie.get().getValue());
            sessionDAO.endSession(sessionId);
        }
    }
}
