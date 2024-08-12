package com.liemartt.dto;

import com.google.gson.annotations.JsonAdapter;
import com.liemartt.entity.Location;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class LocationResponseDto {
    private String name;
    private BigDecimal lat;
    private BigDecimal lon;
    private String country;
    private String state;
    
    public Location getLocation(){
        return new Location(lon, lat, name);
    }
}
