package com.liemartt.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HibernateUtil.configure();
        SchedulerUtil.scheduleSessionDeletion();
    }
    
}
