package com.liemartt.dto;

import com.liemartt.dto.weather.WeatherApiResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherResponse {
    private Long locationId;
    private WeatherApiResponseDto weather;
}
