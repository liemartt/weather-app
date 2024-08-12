package com.liemartt.dto;

import lombok.Data;

@Data
public class WeatherDto {
    private String main;
    private String description;
    private String icon;
}
