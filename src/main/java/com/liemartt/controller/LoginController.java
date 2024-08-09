package com.liemartt.controller;

import com.liemartt.dao.SessionDAO;
import com.liemartt.dao.SessionDAOImpl;
import com.liemartt.dto.UserDto;
import com.liemartt.entity.Session;
import com.liemartt.entity.User;
import com.liemartt.exception.IncorrectPasswordException;
import com.liemartt.exception.UserNotFoundException;
import com.liemartt.service.LoginService;
import com.liemartt.util.ThymeleafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private final LoginService loginService = LoginService.getINSTANCE();
    private final SessionDAO sessionDAO = new SessionDAOImpl();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = ThymeleafUtil.getWebContext(req, resp, getServletContext());
        ThymeleafUtil.process(context, "login.html", resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = ThymeleafUtil.getWebContext(req, resp, getServletContext());
        
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        UserDto userDto = new UserDto(username, password);
        try {
            User user = loginService.validateUser(userDto);
            Session session = sessionDAO.createSession(user);
            resp.addCookie(new Cookie("sessionId", session.getId().toString()));
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (UserNotFoundException | IncorrectPasswordException e) {
            context.setVariable("error", e.getMessage());//TODO responseEntity?
            ThymeleafUtil.process(context, "login.html", resp);
        }
    }
}
