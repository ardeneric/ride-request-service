package com.challenge.ride.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rides")
@Data
public class Ride {
    @Id
    private String id;
    private Integer rideId;
    private Driver driver;

    public Ride(Integer rideId, Driver driver) {
        this.rideId = rideId;
        this.driver = driver;
    }
}