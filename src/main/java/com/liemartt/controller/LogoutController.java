package com.liemartt.controller;

import com.liemartt.dao.session.SessionDAO;
import com.liemartt.dao.session.SessionDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@WebServlet(urlPatterns = "/logout")
public class LogoutController extends HttpServlet {
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Cookie> sessionCookie = Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals("sessionId"))
                .findFirst();
        String sessionId = sessionCookie.get().getValue();
        
        UUID sessionUUID = UUID.fromString(sessionId);
        sessionDAO.delete(sessionUUID);
        
        sessionCookie.get().setMaxAge(0);
        resp.addCookie(sessionCookie.get());
        
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
