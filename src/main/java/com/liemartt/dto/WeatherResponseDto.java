package com.liemartt.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
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
