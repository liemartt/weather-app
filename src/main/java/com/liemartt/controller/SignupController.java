package com.liemartt.controller;

import com.liemartt.dto.UserDto;
import com.liemartt.exception.UsernameAlreadyExistsException;
import com.liemartt.service.SignupService;
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
    private final SignupService signupService = SignupService.getINSTANCE();
    
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
            signupService.signupNewUser(userDto);
        } catch (UsernameAlreadyExistsException e) {
            context.setVariable("error", e.getMessage());//TODO responseEntity?
            ThymeleafUtil.process(context, "signup.html", resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
