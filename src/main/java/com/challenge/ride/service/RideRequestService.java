package com.challenge.ride.service;

import com.challenge.ride.entity.Driver;
import com.challenge.ride.entity.Location;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class RideRequestService {

    private final MongoTemplate mongoTemplate;

    public void findNearestDriver(Location passengerLocation, double maxDistance) {
        Point passengerPoint = new Point(passengerLocation.getLongitude(), passengerLocation.getLatitude());

        Query query = new Query();
        query.addCriteria(Criteria.where("available").is(true)
            .and("location").near(passengerPoint).maxDistance(maxDistance));

        Driver nearestDriver = mongoTemplate.findOne(query, Driver.class, "drivers");

        if (!ObjectUtils.isEmpty(nearestDriver)) {
            mongoTemplate.save(nearestDriver, "nearest-drivers");
        }
    }
}