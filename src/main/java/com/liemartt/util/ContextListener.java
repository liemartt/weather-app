package com.liemartt.util;

import com.liemartt.service.SessionService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HibernateUtil.configure();
        SessionService.getINSTANCE().scheduleSessionDeletion();
    }
    
}
