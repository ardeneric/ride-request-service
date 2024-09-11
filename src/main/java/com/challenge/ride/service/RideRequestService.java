package com.challenge.ride.service;

import com.challenge.ride.config.RabbitConfig;
import com.challenge.ride.entity.Driver;
import com.challenge.ride.model.Location;
import com.challenge.ride.entity.Ride;
import com.challenge.ride.exception.DriverNotFoundException;
import com.challenge.ride.model.RideRequest;
import com.challenge.ride.repository.CustomDriverRepository;
import com.challenge.ride.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideRequestService {
    @Value("${maxDistance}")
    private double maxDistance;
    private final CustomDriverRepository customDriverRepository;
    private final RideRepository rideRepository;
    private final RabbitTemplate rabbitTemplate;

    public void publishRideRequest(RideRequest request) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, request);
    }

    @Cacheable(value = "nearestDrivers", key = "#rideId")
    public Driver findNearestDriver(Integer rideId, Location passengerLocation) {
        log.info("Searching for nearest driver from {} ", passengerLocation);
        Point passengerPoint = new Point(passengerLocation.getLongitude(), passengerLocation.getLatitude());

        Driver nearestDriver = customDriverRepository.findDriverNearLocation(passengerPoint, maxDistance);

        if (!ObjectUtils.isEmpty(nearestDriver)) {
            return rideRepository.save(new Ride(rideId, nearestDriver)).getDriver();
        } else {
            throw new DriverNotFoundException("Driver not found");
        }
    }

    @Cacheable(value = "nearestDrivers", key = "#rideId")
    public Optional<Driver> findNearestDriver(Integer rideId) {
        return Optional.ofNullable(rideRepository.findByRideId(rideId))
                .map(Ride::getDriver);

    }
}