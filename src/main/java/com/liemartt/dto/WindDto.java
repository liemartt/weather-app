package com.liemartt.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WindDto {
    private BigDecimal speed;
    private BigDecimal gust;
}
