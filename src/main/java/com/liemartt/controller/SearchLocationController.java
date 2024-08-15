package com.liemartt.controller;

import com.liemartt.dto.location.LocationApiResponseDto;
import com.liemartt.dto.location.SaveLocationRequestDto;
import com.liemartt.entity.Location;
import com.liemartt.entity.User;
import com.liemartt.exception.*;
import com.liemartt.service.AuthenticationService;
import com.liemartt.service.LocationService;
import com.liemartt.service.WeatherApiService;
import com.liemartt.util.ThymeleafUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(urlPatterns = "/search")
public class SearchLocationController extends BaseController {
    private final AuthenticationService authenticationService = AuthenticationService.getINSTANCE();
    private final WeatherApiService weatherApiService = WeatherApiService.getINSTANCE();
    private final LocationService locationService = LocationService.getINSTANCE();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, WeatherApiException, LocationNotFoundException {
        String locationName = req.getParameter("location");
        if (locationName == null || locationName.isEmpty()) {
            throw new InvalidLocationNameException();
        }
        
        List<LocationApiResponseDto> locations = weatherApiService.searchLocationsByName(locationName);
        
        context.setVariable("locations", locations);
        ThymeleafUtil.process(context, "searchPage.html", resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, LocationExistsException {
        Cookie sessionCookie = findCookieByName(req.getCookies(), "sessionId");
        
        User user = authenticationService.getAuthorizedUser(sessionCookie.getValue())
                .orElseThrow(UserNotAuthorizedException::new);
        
        String name = req.getParameter("name");
        String lat = req.getParameter("lat");
        String lon = req.getParameter("lon");
        
        if (name == null || lat == null || lon == null
                || name.isEmpty() || name.isBlank()
                || lat.isBlank() || lat.isEmpty()
                || lon.isBlank() || lon.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        
        Location location = new Location(new BigDecimal(lon), new BigDecimal(lat), name);
        
        SaveLocationRequestDto dto = new SaveLocationRequestDto(user, location);
        locationService.saveLocation(dto);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
