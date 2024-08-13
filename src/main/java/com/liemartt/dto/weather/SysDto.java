package com.liemartt.dto.weather;

import lombok.Data;

@Data
public class SysDto {
    private String country;
    private Long sunrise;
    private Long sunset;
}
