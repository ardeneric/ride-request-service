package com.challenge.ride.repository.impl;

import com.challenge.ride.entity.Driver;
import com.challenge.ride.entity.Ride;
import com.challenge.ride.repository.CustomDriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CustomDriverRepositoryImpl implements CustomDriverRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Driver findDriverNearLocation(Point location, double maxDistance) {
        Query query = new Query();
        query.addCriteria(Criteria.where("available").is(true)
                .and("location").nearSphere(location).maxDistance(maxDistance));

        return mongoTemplate.findOne(query, Driver.class, "drivers");
    }

    @Override
    public Driver save(Driver driver, String collection) {
        return mongoTemplate.save(driver, "nearest-drivers");
    }

    @Override
    public Ride findById(Integer rideId) {
        return mongoTemplate.findById(rideId, Ride.class);
    }

}
