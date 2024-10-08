package com.challenge.ride.repository;

import com.challenge.ride.entity.Driver;
import org.springframework.data.geo.Point;

public interface CustomDriverRepository {
    Driver findDriverNearLocation(Point location, double maxDistance);
    Driver save(Driver driver, String collection);
}