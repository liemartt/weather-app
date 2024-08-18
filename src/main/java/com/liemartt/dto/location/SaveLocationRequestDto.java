package com.liemartt.dto.location;

import com.liemartt.entity.Location;
import com.liemartt.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SaveLocationRequestDto {
    private User user;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String name;
}
