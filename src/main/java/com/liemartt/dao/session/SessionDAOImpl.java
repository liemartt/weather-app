package com.liemartt.dao.session;

import com.liemartt.entity.Session;
import com.liemartt.exception.DBException;
import com.liemartt.util.HibernateUtil;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionDAOImpl implements SessionDAO {
    @Override
    public void save(Session userSession) {
        org.hibernate.Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.persist(userSession);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DBException();
        }
    }
    
    @Override
    public void delete(UUID userSession) {
        org.hibernate.Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.createMutationQuery("delete from Session where id=:id")
                    .setParameter("id", userSession)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DBException();
        }
    }
    
    @Override
    public void deleteExpiredSessions() {
        LocalDateTime time = LocalDateTime.now();
        org.hibernate.Session session = HibernateUtil.getSession();
        try {
            System.out.println("deleting expired sessions " + time);
            session.beginTransaction();
            session.createMutationQuery("DELETE from Session where expiresAt<:time")
                    .setParameter("time", time)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DBException();
        }
    }
    
    @Override
    public Optional<Session> findById(UUID sessionId) {
        org.hibernate.Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            Session userSession = session.createSelectionQuery("from Session where id=:uuid", Session.class)
                    .setParameter("uuid", sessionId)
                    .getSingleResult();
            session.getTransaction().commit();
            return Optional.ofNullable(userSession);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DBException();
        }
    }
}
