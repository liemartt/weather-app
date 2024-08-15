package com.liemartt.controller;

import com.liemartt.dto.UserDto;
import com.liemartt.entity.Session;
import com.liemartt.exception.user.IncorrectPasswordException;
import com.liemartt.exception.user.UserNotFoundException;
import com.liemartt.service.AuthenticationService;
import com.liemartt.util.ThymeleafUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginController extends BaseController {
    private final AuthenticationService authenticationService = AuthenticationService.getINSTANCE();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ThymeleafUtil.process(context, "login.html", resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, UserNotFoundException, IncorrectPasswordException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        if (username == null || password == null || username.isEmpty() || username.isBlank() || password.isEmpty() || password.isBlank()) {
            throw new IncorrectPasswordException();
        }
        
        UserDto userDto = new UserDto(username, password);
        
        Session session = authenticationService.loginUser(userDto);
        resp.addCookie(new Cookie("sessionId", session.getId().toString()));
        resp.sendRedirect(req.getContextPath() + "/");
        
    }
    
}
