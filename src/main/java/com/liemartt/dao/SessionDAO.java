package com.liemartt.dao;

import com.liemartt.entity.Session;
import com.liemartt.entity.User;

import java.util.Optional;

public interface SessionDAO {
    Session createSession(User user);
    
    void endSession(Session session);
    
    void endAllExpiredSessions();
    
    Optional<Session> getSessionByUUID(String sessionUUID);
}
