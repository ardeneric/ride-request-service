package com.challenge.ride.listener;

import com.challenge.ride.config.RabbitConfig;
import com.challenge.ride.model.RideRequest;
import com.challenge.ride.service.RideRequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class RideRequestListener {

    private final RideRequestService rideRequestService;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void handleRideRequest(RideRequest rideRequest) {
        log.info("Received ride request :: {} ", rideRequest);
        try {
            rideRequestService.findNearestDriver(rideRequest.getRideId(), rideRequest.getPassengerLocation());
        } catch (Exception ex) {
            log.error("Error occurred :: ", ex);
        }
    }
}
