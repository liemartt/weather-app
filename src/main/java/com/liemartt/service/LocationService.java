package com.liemartt.service;

import com.liemartt.dao.location.LocationDAO;
import com.liemartt.dao.location.LocationDAOImpl;
import com.liemartt.dto.location.DeleteLocationRequestDto;
import com.liemartt.dto.location.SaveLocationRequestDto;
import com.liemartt.entity.Location;
import com.liemartt.entity.User;
import com.liemartt.exception.location.LocationExistsException;
import lombok.Getter;
import java.util.Optional;

public class LocationService {
    @Getter
    private final static LocationService INSTANCE = new LocationService();
    private final LocationDAO locationDAO;
    
    private LocationService() {
        locationDAO = new LocationDAOImpl();
    }
    
    
    public void saveLocation(SaveLocationRequestDto dto) throws LocationExistsException {
        Location location = dto.getLocation();
        User user = dto.getUser();
        
        location.setUser(user);
        user.addLocation(location);
        
        locationDAO.save(location);
    }
    
    public void deleteLocation(DeleteLocationRequestDto dto) {
        User user = dto.getUser();
        Optional<Location> locationToDelete =
                user.getLocations()
                        .stream()
                        .filter(userLocation -> userLocation.getId().equals(dto.getLocationId()))
                        .findFirst();

        if (locationToDelete.isPresent()) {
            Location location = locationToDelete.get();
            
            user.getLocations().remove(location);
            locationDAO.delete(location);
        }
    }
    
}
