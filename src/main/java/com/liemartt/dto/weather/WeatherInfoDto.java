package com.liemartt.dto.weather;

import lombok.Data;

@Data
public class WeatherInfoDto {
    private String main;
    private String description;
    private String icon;
}
