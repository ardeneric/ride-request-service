package com.challenge.ride.controller;

import com.challenge.ride.entity.Driver;
import com.challenge.ride.model.RideRequest;
import com.challenge.ride.service.RideRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
@Tag(name = "Ride Request Controller", description = "APIs to manage ride requests and find nearest drivers.")
public class RideController {

    private final RideRequestService rideRequestService;

    @PostMapping
    @Operation(summary = "Submit a new ride request", description = "Sends a ride request to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Request accepted and processed"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    public ResponseEntity<String> sendRideRequest(@RequestBody @Valid RideRequest rideRequest) {
        rideRequestService.publishRideRequest(rideRequest);
        return ResponseEntity.accepted().body("Searching for driver...");
    }

    @GetMapping("/{rideId}")
    @Operation(summary = "Get nearest driver", description = "Fetches the nearest available driver for the ride request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nearest driver found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Driver.class))),
            @ApiResponse(responseCode = "404", description = "No driver found", content = @Content)
    })
    public ResponseEntity<Driver> getNearestDriver(@PathVariable Integer rideId) {
        Optional<Driver> result = rideRequestService.findNearestDriver(rideId);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
