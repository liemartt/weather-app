package com.liemartt.dto;

import com.liemartt.dto.weather.MainDto;
import com.liemartt.dto.weather.SysDto;
import com.liemartt.dto.weather.WeatherDto;
import com.liemartt.dto.weather.WindDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WeatherResponseDto {
    private List<WeatherDto> weather;
    private MainDto main;
    private String visibility;
    private WindDto wind;
    private SysDto sys;
    private String name;
}
