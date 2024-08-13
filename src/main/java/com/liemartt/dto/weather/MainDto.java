package com.liemartt.dto.weather;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MainDto {
    private BigDecimal temp;
    @SerializedName("feels_like")
    private BigDecimal feelsLike;
    @SerializedName("temp_min")
    private BigDecimal minTemp;
    @SerializedName("temp_max")
    private BigDecimal maxTemp;
}
