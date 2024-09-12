package com.challenge.ride.repository;

import com.challenge.ride.entity.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RideRepository extends MongoRepository<Ride, String> {
    Ride findByRiderIdBefore(Integer riderId, LocalDateTime time);
}