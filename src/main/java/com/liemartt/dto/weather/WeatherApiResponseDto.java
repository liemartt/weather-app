package com.liemartt.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WeatherApiResponseDto {
    private List<WeatherInfoDto> weather;
    private MainDto main;
    private WindDto wind;
    private SysDto sys;
    private String name;
    private String visibility;
    private Coordinate coord;
}
