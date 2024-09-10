package com.challenge.ride.repository;

import com.challenge.ride.entity.Driver;
import com.challenge.ride.entity.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends MongoRepository<Ride, Integer> {
}