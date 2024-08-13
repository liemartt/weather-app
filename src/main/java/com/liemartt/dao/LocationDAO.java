package com.liemartt.dao;

import com.liemartt.entity.Location;

import java.util.List;

public interface LocationDAO {
    void save(Location location);
    void delete(Location location);
}
