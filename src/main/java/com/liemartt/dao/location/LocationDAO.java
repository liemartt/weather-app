package com.liemartt.dao.location;

import com.liemartt.entity.Location;


public interface LocationDAO {
    void save(Location location);
    void delete(Location location);
}
