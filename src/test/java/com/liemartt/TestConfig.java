package com.liemartt;

import com.liemartt.entity.Location;
import com.liemartt.entity.Session;
import com.liemartt.entity.User;
import lombok.SneakyThrows;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class TestConfig {
    private static final Configuration cfg = configure();
    private static final SessionFactory sessionFactory = buildSessionFactory();
    
    @SneakyThrows
    public static Configuration configure() {
        Configuration configuration = new Configuration();
        Properties properties = new Properties();
        properties.load(TestConfig.class.getResourceAsStream("/hibernate.properties"));
        
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(User.class).addAnnotatedClass(Session.class).addAnnotatedClass(Location.class);
        return configuration;
    }
    
    public static org.hibernate.Session getSession() {
        return sessionFactory.openSession();
    }
    
    private static SessionFactory buildSessionFactory() {
        return cfg.buildSessionFactory();
    }
}
