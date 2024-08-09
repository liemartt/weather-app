package com.liemartt.dao;

import com.liemartt.entity.Session;
import com.liemartt.entity.User;
import com.liemartt.util.HibernateUtil;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SessionDAOImpl implements SessionDAO {
    @Override
    public Session createSession(User user) {
        UUID uuid = UUID.randomUUID();
        Session userSession = new Session(uuid, user, LocalDateTime.now().plusMinutes(30));
        try (org.hibernate.Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(userSession);
            session.getTransaction().commit();
            return userSession;
        }
    }
    
    @Override
    public void endSession(Session userSession) {
        try (org.hibernate.Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.remove(userSession);
            session.getTransaction().commit();
        }
    }
    
    @Override
    public List<Session> getAllExpiredSessions() {
        LocalDateTime time = LocalDateTime.now();
        try (org.hibernate.Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            List<Session> expiredSessions = session.createSelectionQuery("from Session where expiresAt<:time", Session.class)
                    .setParameter("time", time)
                    .getResultList();
            session.getTransaction().commit();
            return expiredSessions;
        }
    }
    
    @Override
    public Optional<Session> getSessionByUUID(String sessionUUID) {
        try (org.hibernate.Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Session userSession = session.createSelectionQuery("from Session where id=:uuid", Session.class)
                    .setParameter("uuid", UUID.fromString(sessionUUID))
                    .getSingleResult();
            session.getTransaction().commit();
            return Optional.ofNullable(userSession);
        } catch (NoResultException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
