package com.liemartt.dao;

import com.liemartt.entity.Location;
import com.liemartt.exception.DBException;
import com.liemartt.exception.LocationExistsException;
import com.liemartt.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

public class LocationDAOImpl implements LocationDAO {
    @Override
    public void save(Location location) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.persist(location);
            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            throw new LocationExistsException();
        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DBException("DB is unavailable");
        }
    }
    
    @Override
    public void delete(Location location) {
        Session session = HibernateUtil.getSession();
        try {
            session.beginTransaction();
            session.remove(location);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new DBException("DB is unavailable");
        }
    }
    
}