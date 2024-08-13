package com.liemartt.dto.location;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocationApiResponseDto {
    private String name;
    private BigDecimal lat;
    private BigDecimal lon;
    private String country;
    private String state;
}
