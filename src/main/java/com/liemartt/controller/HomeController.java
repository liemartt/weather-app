package com.liemartt.controller;

import com.liemartt.dto.WeatherWebDto;
import com.liemartt.dto.location.DeleteLocationRequestDto;
import com.liemartt.dto.weather.WeatherApiResponseDto;
import com.liemartt.entity.Location;
import com.liemartt.entity.User;
import com.liemartt.service.AuthenticationService;
import com.liemartt.service.LocationService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/")
public class HomeController extends HttpServlet {
    private final AuthenticationService authenticationService = AuthenticationService.getINSTANCE();
    private final WeatherApiService weatherApiService = WeatherApiService.getINSTANCE();
    private final LocationService locationService = LocationService.getINSTANCE();
    
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
        List<WeatherWebDto> weather = new ArrayList<>();
        authorizedUser.getLocations()
                .forEach(location -> weather.add(createWeatherWebDto(location)));
        
        context.setVariable("user", authorizedUser);
        context.setVariable("weatherList", weather);
        ThymeleafUtil.process(context, "index.html", resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = ThymeleafUtil.getWebContext(req, resp, req.getServletContext());
        String sessionId = Arrays.stream(req.getCookies()).filter(cookie -> cookie.getName().equals("sessionId"))
                .findFirst().get().getValue();
        String locationId = req.getParameter("location_id");
        
        User user = authenticationService.getAuthorizedUser(sessionId).get();
        
        DeleteLocationRequestDto dto = new DeleteLocationRequestDto(user, Long.valueOf(locationId));
        locationService.deleteLocation(dto);
        this.doGet(req, resp);
    }
    private WeatherWebDto createWeatherWebDto(Location location){
        WeatherApiResponseDto response = weatherApiService.searchWeatherByLocation(location);
        return new WeatherWebDto(location.getId(), response);
    }
}
