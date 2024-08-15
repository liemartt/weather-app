package com.liemartt.dao.session;

import com.liemartt.entity.Session;
import com.liemartt.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionDAOImpl implements SessionDAO {
    private static final Logger log = LoggerFactory.getLogger(SessionDAOImpl.class);
    
    @Override
    public void save(Session userSession) {
        try (org.hibernate.Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(userSession);
            session.getTransaction().commit();
        }
    }
    
    @Override
    public void delete(UUID userSession) {
        try (org.hibernate.Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.createMutationQuery("delete from Session where id=:id")
                    .setParameter("id", userSession)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }
    
    @Override
    public void deleteExpiredSessions() {
        LocalDateTime time = LocalDateTime.now();
        try (org.hibernate.Session session = HibernateUtil.getSession()) {
            System.out.println("deleting expired sessions " + time);
            session.beginTransaction();
            session.createMutationQuery("DELETE from Session where expiresAt<:time")
                    .setParameter("time", time)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }
    
    @Override
    public Optional<Session> findById(UUID sessionId) {
        try (org.hibernate.Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Session userSession = session.createSelectionQuery("from Session where id=:uuid", Session.class)
                    .setParameter("uuid", sessionId)
                    .getSingleResult();
            session.getTransaction().commit();
            return Optional.ofNullable(userSession);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}