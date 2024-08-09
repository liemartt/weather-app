package com.liemartt.dao;

import com.liemartt.entity.Session;
import com.liemartt.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionDAO {
    Session createSession(User user);
    
    void endSession(Session session);
    
    List<Session> getAllExpiredSessions();
    
    Optional<Session> getSessionByUUID(String sessionUUID);
}
