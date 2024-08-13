package com.liemartt.dto.location;

import com.liemartt.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteLocationRequestDto {
    private User user;
    private Long locationId;
}
