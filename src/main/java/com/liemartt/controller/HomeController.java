package com.liemartt.controller;

import com.liemartt.entity.User;
import com.liemartt.exception.UserNotAuthorizedException;
import com.liemartt.service.SessionService;
import com.liemartt.util.ThymeleafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet(urlPatterns = "/")
public class HomeController extends HttpServlet {
    private final SessionService sessionService = SessionService.getINSTANCE();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = ThymeleafUtil.getWebContext(req, resp, getServletContext());
        try {
            User user = sessionService.getAuthorizedUser(req.getCookies());
        } catch (UserNotAuthorizedException e) {
            context.setVariable("error", "Please log in or sign up");
            ThymeleafUtil.process(context, "index.html", resp);
            return;
        }
        
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
    }
}
