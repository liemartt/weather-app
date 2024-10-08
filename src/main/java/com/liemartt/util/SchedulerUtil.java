package com.liemartt.util;

import com.liemartt.dao.session.SessionDAO;
import com.liemartt.dao.session.SessionDAOImpl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerUtil {
    private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final static SessionDAO sessionDAO = new SessionDAOImpl();
    
    public static void scheduleSessionDeletion() {
        scheduler.scheduleAtFixedRate(sessionDAO::deleteExpiredSessions, 0, 5, TimeUnit.MINUTES);
    }
}
