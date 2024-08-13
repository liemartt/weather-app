package com.liemartt.dto.weather;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Coordinate {
    private BigDecimal lon;
    private BigDecimal lat;
}
