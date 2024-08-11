package com.liemartt.dto;

import lombok.Data;

@Data
public class WeatherDto {
    private String main;
    private String description;
    private Double temp;
    private Double wind_speed;
}
