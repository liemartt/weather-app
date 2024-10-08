package com.liemartt.controller;

import com.liemartt.dto.UserDto;
import com.liemartt.exception.user.UsernameExistsException;
import com.liemartt.service.AuthenticationService;
import com.liemartt.util.ThymeleafUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/signup")
public class SignupController extends BaseController {
    private final AuthenticationService authenticationService = AuthenticationService.getINSTANCE();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ThymeleafUtil.process(context, "signup.html", resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, UsernameExistsException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        if (username == null || password == null || username.isEmpty() || username.isBlank() || password.isEmpty() || password.isBlank()) {
            this.doGet(req, resp);
            return;
        }
        
        UserDto userDto = new UserDto(username, password);
        
        authenticationService.signupNewUser(userDto);
        
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
