package com.liemartt.controller;

import com.liemartt.dto.WeatherWebDto;
import com.liemartt.dto.location.DeleteLocationRequestDto;
import com.liemartt.dto.weather.WeatherApiResponseDto;
import com.liemartt.entity.Location;
import com.liemartt.entity.Session;
import com.liemartt.entity.User;
import com.liemartt.exception.UserNotAuthorizedException;
import com.liemartt.service.AuthenticationService;
import com.liemartt.service.LocationService;
import com.liemartt.service.WeatherApiService;
import com.liemartt.util.ThymeleafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(urlPatterns = "/")
public class HomeController extends BaseController {
    private final AuthenticationService authenticationService = AuthenticationService.getINSTANCE();
    private final WeatherApiService weatherApiService = WeatherApiService.getINSTANCE();
    private final LocationService locationService = LocationService.getINSTANCE();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie sessionCookie = findCookieByName(req.getCookies(), "sessionId")
                .orElseThrow(UserNotAuthorizedException::new);
        String sessionId = sessionCookie.getValue();
        
        User authorizedUser = authenticationService
                .getAuthorizedUser(sessionId)
                .orElseThrow(() -> new UserNotAuthorizedException("Session " + sessionId + " has expired, please log in"));
        
        List<WeatherWebDto> weather = new ArrayList<>();
        authorizedUser.getLocations()
                .forEach(location -> weather.add(createWeatherWebDto(location)));
        
        context.setVariable("user", authorizedUser);
        context.setVariable("weatherList", weather);
        ThymeleafUtil.process(context, "index.html", resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie sessionCookie = findCookieByName(req.getCookies(), "sessionId")
                .orElseThrow(UserNotAuthorizedException::new);
        
        String sessionId = sessionCookie.getValue();
        String locationId = req.getParameter("location-id");
        
        User authorizedUser = authenticationService
                .getAuthorizedUser(sessionId)
                .orElseThrow(() -> new UserNotAuthorizedException("Session " + sessionId + "has expired, please log in"));
        
        DeleteLocationRequestDto dto = new DeleteLocationRequestDto(authorizedUser, Long.valueOf(locationId));
        locationService.deleteLocation(dto);
        this.doGet(req, resp);
    }
    
    private WeatherWebDto createWeatherWebDto(Location location) {
        WeatherApiResponseDto response = weatherApiService.searchWeatherByLocation(location);
        response.setName(location.getName());
        return new WeatherWebDto(location.getId(), response);
    }
}
