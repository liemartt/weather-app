package com.liemartt.dto.weather;

import lombok.Data;

@Data
public class WeatherDto {
    private String main;
    private String description;
    private String icon;
}
