package com.liemartt.dao;

import com.liemartt.entity.Session;
import com.liemartt.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface SessionDAO {
    Session createSession(User user);
    
    void endSession(UUID sessionId);
    
    void endAllExpiredSessions();
    
    Optional<Session> getSessionByUUID(UUID sessionUUID);
}
