package com.liemartt.controller;

import com.liemartt.dto.LocationResponseDto;
import com.liemartt.dto.SaveLocationRequestDto;
import com.liemartt.entity.Location;
import com.liemartt.entity.User;
import com.liemartt.exception.LocationExistsException;
import com.liemartt.exception.LocationNotFoundException;
import com.liemartt.exception.WeatherApiException;
import com.liemartt.service.AuthenticationService;
import com.liemartt.service.LocationService;
import com.liemartt.service.WeatherApiService;
import com.liemartt.util.ThymeleafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(urlPatterns = "/search")
public class SearchLocationController extends HttpServlet {
    private final AuthenticationService authenticationService = AuthenticationService.getINSTANCE();
    private final WeatherApiService weatherApiService = WeatherApiService.getINSTANCE();
    private final LocationService locationService = LocationService.getINSTANCE();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = ThymeleafUtil.getWebContext(req, resp, req.getServletContext());
        String locationName = req.getParameter("location");
        
        try {
            List<LocationResponseDto> locations = weatherApiService.searchLocationsByName(locationName);
            context.setVariable("locations", locations);
        } catch (LocationNotFoundException | WeatherApiException e) {
            context.setVariable("message", e.getMessage());
        }
        
        ThymeleafUtil.process(context, "searchPage.html", resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = ThymeleafUtil.getWebContext(req, resp, req.getServletContext());
        String sessionId = Arrays.stream(req.getCookies()).filter(cookie -> cookie.getName().equals("sessionId"))
                .findFirst().get().getValue();
        
        User user = authenticationService.getAuthorizedUser(sessionId).get();
        
        String locationJson = req.getParameter("locationJson");
        Location location = locationService.getLocationFromJson(locationJson);
        
        SaveLocationRequestDto dto = new SaveLocationRequestDto(user, location);
        try {
            locationService.saveLocation(dto);
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (LocationExistsException e) {
            context.setVariable("message", e.getMessage());
            ThymeleafUtil.process(context, "searchPage.html", resp);
        }
        
    }
}
