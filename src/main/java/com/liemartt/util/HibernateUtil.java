package com.liemartt.util;

import com.liemartt.entity.Location;
import com.liemartt.entity.Session;
import com.liemartt.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final Configuration cfg = configure();
    private static final SessionFactory sessionFactory = buildSessionFactory();
    
    public static Configuration configure() {
        return new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Session.class)
                .addAnnotatedClass(Location.class);
    }
    
    public static org.hibernate.Session getSession() {
        return sessionFactory.openSession();
    }
    
    private static SessionFactory buildSessionFactory() {
        return cfg.buildSessionFactory();
    }
    
    
}
