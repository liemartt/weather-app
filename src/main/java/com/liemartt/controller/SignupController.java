package com.liemartt.controller;

import com.liemartt.dto.UserDto;
import com.liemartt.exception.UsernameExistsException;
import com.liemartt.service.AuthenticationService;
import com.liemartt.util.ThymeleafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet(urlPatterns = "/signup")
public class SignupController extends HttpServlet {
    private final AuthenticationService authenticationService = AuthenticationService.getINSTANCE();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = ThymeleafUtil.getWebContext(req, resp, getServletContext());
        ThymeleafUtil.process(context, "signup.html", resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = ThymeleafUtil.getWebContext(req, resp, getServletContext());
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        UserDto userDto = new UserDto(username, password);
        try {
            authenticationService.signupNewUser(userDto);
        } catch (UsernameExistsException e) {
            context.setVariable("error", e.getMessage());//TODO responseEntity?
            ThymeleafUtil.process(context, "signup.html", resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
