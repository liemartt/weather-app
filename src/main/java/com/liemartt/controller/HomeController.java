package com.liemartt.controller;

import com.liemartt.dto.LocationResponseDto;
import com.liemartt.entity.User;
import com.liemartt.service.AuthenticationService;
import com.liemartt.service.WeatherApiService;
import com.liemartt.util.ThymeleafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/")
public class HomeController extends HttpServlet {
    private final AuthenticationService authenticationService = AuthenticationService.getINSTANCE();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = ThymeleafUtil.getWebContext(req, resp, getServletContext());
        Optional<Cookie> sessionCookie = Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals("sessionId"))
                .findFirst();
        
        Optional<String> sessionId = sessionCookie.map(Cookie::getValue);
        if (sessionId.isEmpty()) {
            context.setVariable("error", "Please log in or sign up");
            ThymeleafUtil.process(context, "index.html", resp);
            return;
        }
        
        Optional<User> userOptional = authenticationService.getAuthorizedUser(sessionId.get());
        if (userOptional.isEmpty()) {
            context.setVariable("error", "Your session has expired, please log in again");
            ThymeleafUtil.process(context, "index.html", resp);
            return;
        }
        
        User authorizedUser = userOptional.get();
        context.setVariable("user", authorizedUser);
        ThymeleafUtil.process(context, "index.html", resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String locationName = req.getParameter("locationName");
//        try {
//            List<LocationResponseDto> locationResponseDto = new WeatherApiService().searchLocationsByName(locationName);
//            String response = new WeatherApiService().searchWeatherByLocation(locationResponseDto.get(0).getLocation());
//            resp.setContentType("application/json");
//            resp.getWriter().write(response);
//        } catch (URISyntaxException | InterruptedException  e) {
//            e.printStackTrace();
//        }
    }
}
