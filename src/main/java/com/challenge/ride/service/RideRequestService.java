package com.challenge.ride.service;

import com.challenge.ride.entity.Driver;
import com.challenge.ride.entity.Location;
import com.challenge.ride.model.RideRequest;
import com.challenge.ride.repository.CustomDriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideRequestService {
    public static final String NEAREST_DRIVERS = "nearest-drivers";

    private final CustomDriverRepository customDriverRepository;

    private final RabbitTemplate rabbitTemplate;

    public void publishRideRequest(RideRequest request) {
        rabbitTemplate.convertAndSend("rideRequests", request);
    }

    public Driver findNearestDriver(Location passengerLocation) {
        log.info("Finding nearest driver from {} ", passengerLocation);
        Point passengerPoint = new Point(passengerLocation.getLongitude(), passengerLocation.getLatitude());

        Driver nearestDriver = customDriverRepository.findDriverNearLocation(passengerPoint,1000);

        if (!ObjectUtils.isEmpty(nearestDriver)) {
            customDriverRepository.save(nearestDriver, NEAREST_DRIVERS);
        }
        return nearestDriver;
    }
}