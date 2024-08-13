package com.liemartt.dao;

import com.liemartt.entity.Session;

import java.util.Optional;
import java.util.UUID;

public interface SessionDAO {
    void save(Session session);
    
    void delete(UUID sessionId);
    
    void deleteExpiredSessions();
    
    Optional<Session> findById(UUID sessionId);
}
