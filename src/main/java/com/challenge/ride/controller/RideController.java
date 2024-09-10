package com.challenge.ride.controller;

import com.challenge.ride.entity.Driver;
import com.challenge.ride.entity.Ride;
import com.challenge.ride.model.RideRequest;
import com.challenge.ride.service.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideRequestService rideRequestService;

    @PostMapping
    public ResponseEntity<String> sendRideRequest(@RequestBody RideRequest rideRequest) {
        rideRequestService.publishRideRequest(rideRequest);
        return ResponseEntity.accepted().body("Searching for driver...");
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<Driver> getNearestDriver(@PathVariable Integer rideId) {
        Ride result = rideRequestService.findNearestDriver(rideId);
        if (!ObjectUtils.isEmpty(result)){
            return ResponseEntity.ok(result.getDriver());
        }
        return ResponseEntity.notFound().build();
    }
}
