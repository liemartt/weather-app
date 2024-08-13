package com.liemartt.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocationResponseDto {
    private String name;
    private BigDecimal lat;
    private BigDecimal lon;
    private String country;
    private String state;
}
