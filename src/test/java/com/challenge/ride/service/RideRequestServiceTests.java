package com.challenge.ride.service;

import com.challenge.ride.entity.Driver;
import com.challenge.ride.entity.Location;
import com.challenge.ride.model.RideRequest;
import com.challenge.ride.repository.CustomDriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.geo.Point;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideRequestServiceTests {

    @Mock
    private CustomDriverRepository customDriverRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RideRequestService rideRequestService;

    @Test
    void testPublishRideRequest() {
        RideRequest rideRequest = new RideRequest();

        rideRequestService.publishRideRequest(rideRequest);

        verify(rabbitTemplate, times(1)).convertAndSend("rideRequests", rideRequest);
    }

    @Test
    void testFindNearestDriver() {
        // Given
        Location passengerLocation = new Location(32.7777, -0.2260);
        Point passengerPoint = new Point(passengerLocation.getLongitude(), passengerLocation.getLatitude());
        Driver driver = new Driver();
        driver.setId("driver1");
        driver.setLocation(passengerPoint);
        driver.setAvailable(true);

        when(customDriverRepository.findDriverNearLocation(any(Point.class), anyDouble())).thenReturn(driver);

        // When
        rideRequestService.findNearestDriver(passengerLocation);

        // Then
        verify(customDriverRepository, times(1)).findDriverNearLocation(any(Point.class), anyDouble());
        verify(customDriverRepository, times(1)).save(driver, "nearest-drivers");
    }

    @Test
    void testFindNearestDriverWhenNoDriverFound() {
        // Given
        Location passengerLocation = new Location(32.7777, -0.2260);
        when(customDriverRepository.findDriverNearLocation(any(), anyDouble())).thenReturn(null);

        // When
        rideRequestService.findNearestDriver(passengerLocation);

        // Then
        verify(customDriverRepository, times(1)).findDriverNearLocation(any(Point.class), anyDouble());
        verify(customDriverRepository, never()).save(any(Driver.class), anyString());
    }
}