package com.challenge.ride.repository;

import com.challenge.ride.entity.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends MongoRepository<Driver, String> {
    List<Driver> findByAvailableTrue();
}