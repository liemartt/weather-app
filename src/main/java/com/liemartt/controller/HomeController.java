package com.liemartt.controller;

import com.liemartt.entity.User;
import com.liemartt.service.SessionService;
import com.liemartt.util.ThymeleafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/")
public class HomeController extends HttpServlet {
    private final SessionService sessionService = SessionService.getINSTANCE();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = ThymeleafUtil.getWebContext(req, resp, getServletContext());
        Optional<User> userOptional = sessionService.getAuthorizedUser(req.getCookies());
        if (userOptional.isEmpty()) {
            context.setVariable("error", "Please log in or sign up");
            ThymeleafUtil.process(context, "index.html", resp);
            return;
        }
        User authorizedUser = userOptional.get();
        context.setVariable("user", authorizedUser);
        ThymeleafUtil.process(context, "index.html", resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
    }
}
