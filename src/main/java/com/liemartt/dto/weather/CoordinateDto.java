package com.liemartt.dto.weather;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoordinateDto {
    private BigDecimal lon;
    private BigDecimal lat;
}
