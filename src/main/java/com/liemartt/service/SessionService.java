package com.liemartt.service;

import com.liemartt.dao.SessionDAO;
import com.liemartt.dao.SessionDAOImpl;
import com.liemartt.entity.Session;
import com.liemartt.entity.User;
import jakarta.servlet.http.Cookie;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionService {
    @Getter
    private final static SessionService INSTANCE = new SessionService();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    
    private SessionService() {
    }
    
    public void scheduleSessionDeletion() {
        scheduler.scheduleAtFixedRate(sessionDAO::endAllExpiredSessions, 0, 5, TimeUnit.MINUTES);
    }
    
    public Optional<User> getAuthorizedUser(Cookie[] cookies) {
        Optional<Cookie> sessionCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("sessionId"))
                .findFirst();
        if (sessionCookie.isPresent()) {
            String sessionId = sessionCookie.get().getValue();
            Optional<Session> session = sessionDAO.getSessionByUUID(sessionId);
            if (session.isPresent()) {
                return Optional.of(session.get().getUser());
            }
        }
        return Optional.empty();
    }
}

