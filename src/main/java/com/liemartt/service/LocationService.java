package com.liemartt.service;

import com.google.gson.Gson;
import com.liemartt.dao.LocationDAO;
import com.liemartt.dao.LocationDAOImpl;
import com.liemartt.dto.LocationResponseDto;
import com.liemartt.dto.SaveLocationRequestDto;
import com.liemartt.entity.Location;
import com.liemartt.entity.User;
import com.liemartt.exception.LocationExistsException;
import lombok.Getter;

public class LocationService {
    @Getter
    private final static LocationService INSTANCE = new LocationService();
    private final LocationDAO locationDAO;
    
    private LocationService() {
        locationDAO = new LocationDAOImpl();
    }

//    public void saveLocation(SaveLocationRequestDto dto)
    
    
    public Location getLocationFromJson(String json) {
        Gson gson = new Gson();
        LocationResponseDto dto = gson.fromJson(json, LocationResponseDto.class);
        
        return new Location(dto.getLon(), dto.getLat(), dto.getName());
    }
    
    public void saveLocation(SaveLocationRequestDto dto) throws LocationExistsException {
        Location location = dto.getLocation();
        User user = dto.getUser();
        
        location.setUser(user);
        user.addLocation(location);
        
        locationDAO.save(location);
    }
}
