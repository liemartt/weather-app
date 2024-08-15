package com.liemartt.controller;

import com.liemartt.dao.session.SessionDAO;
import com.liemartt.dao.session.SessionDAOImpl;
import com.liemartt.exception.UserNotAuthorizedException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = "/logout")
public class LogoutController extends BaseController {
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, UserNotAuthorizedException {
        Cookie sessionCookie = findCookieByName(req.getCookies(), "sessionId");
        
        UUID sessionUUID = UUID.fromString(sessionCookie.getValue());
        sessionDAO.delete(sessionUUID);
        
        sessionCookie.setMaxAge(0);
        resp.addCookie(sessionCookie);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
