package com.liemartt.dao;

import com.liemartt.entity.Location;


public interface LocationDAO {
    void save(Location location);
    void delete(Location location);
}
