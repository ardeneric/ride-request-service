package com.challenge.ride.controller;

import com.challenge.ride.entity.Driver;
import com.challenge.ride.model.RideRequest;
import com.challenge.ride.service.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideRequestService rideRequestService;

    @PostMapping
    public ResponseEntity<String> submitRideRequest(@RequestBody RideRequest rideRequest) {
        rideRequestService.findNearestDriver(rideRequest.getPassengerLocation(), 5000);
        return ResponseEntity.ok("Searching for driver...");
    }
}
