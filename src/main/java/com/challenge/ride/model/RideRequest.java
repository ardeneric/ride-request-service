package com.challenge.ride.model;

import com.challenge.ride.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequest {
    private Location passengerLocation;
}