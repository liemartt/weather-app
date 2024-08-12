package com.liemartt.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoordinateDto {
    private BigDecimal lon;
    private BigDecimal lat;
}
