package com.liemartt.dto.location;

import com.liemartt.entity.Location;
import com.liemartt.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveLocationRequestDto {
    private User user;
    private Location location;
}
