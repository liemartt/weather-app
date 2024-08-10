package com.liemartt.util;

import com.liemartt.dao.SessionDAO;
import com.liemartt.dao.SessionDAOImpl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerUtil {
    private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final static SessionDAO sessionDAO = new SessionDAOImpl();
    
    public static void scheduleSessionDeletion() {
        scheduler.scheduleAtFixedRate(sessionDAO::endAllExpiredSessions, 0, 5, TimeUnit.MINUTES);
    }
}
