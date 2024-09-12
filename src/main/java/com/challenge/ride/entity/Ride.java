package com.challenge.ride.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "rides")
@Data
public class Ride {
    @Id
    private String id;
    private Integer riderId;
    private Driver driver;
    @CreatedDate
    private LocalDateTime createdAt;

    public Ride(Integer riderId, Driver driver) {
        this.riderId = riderId;
        this.driver = driver;
        this.createdAt = LocalDateTime.now();
    }
}