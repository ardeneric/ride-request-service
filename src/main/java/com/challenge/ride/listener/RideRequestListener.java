package com.challenge.ride.listener;

import com.challenge.ride.model.RideRequest;
import com.challenge.ride.service.RideRequestService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RideRequestListener {

    private final RideRequestService rideRequestService;

    @RabbitListener(queues = "rideRequests")
    public void handleRideRequest(RideRequest rideRequest) {
        rideRequestService.findNearestDriver(rideRequest.getPassengerLocation(), 5000);
    }
}
