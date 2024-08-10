package com.liemartt.util;

import com.liemartt.entity.User;
import com.liemartt.service.AuthenticationService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebFilter(urlPatterns = {"/login", "/signup"})
public class AuthenticatedUserRedirectFilter implements Filter {
    private final AuthenticationService authenticationService = AuthenticationService.getINSTANCE();
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        Optional<User> userOptional = authenticationService.getAuthorizedUser(req.getCookies()); //todo create validateSession() or smth
        if (userOptional.isPresent()) {
            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            chain.doFilter(request, response);
        }
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    
    @Override
    public void destroy() {}
}
