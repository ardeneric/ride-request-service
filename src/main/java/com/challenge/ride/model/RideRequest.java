package com.challenge.ride.model;

import com.challenge.ride.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideRequest {
    private Location passengerLocation;
}