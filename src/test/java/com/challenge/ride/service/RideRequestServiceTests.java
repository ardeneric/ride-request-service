package com.challenge.ride.service;

import com.challenge.ride.config.RabbitConfig;
import com.challenge.ride.entity.Driver;
import com.challenge.ride.model.Location;
import com.challenge.ride.entity.Ride;
import com.challenge.ride.model.RideRequest;
import com.challenge.ride.repository.CustomDriverRepository;
import com.challenge.ride.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.geo.Point;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideRequestServiceTests {

    @Mock
    private CustomDriverRepository customDriverRepository;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RideRequestService rideRequestService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(rideRequestService, "maxDistance", 1000);
    }

    @Test
    void testPublishRideRequest() {
        RideRequest rideRequest = new RideRequest();

        rideRequestService.publishRideRequest(rideRequest);

        verify(rabbitTemplate, times(1)).convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, rideRequest);
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

        final Ride ride = new Ride(1, driver);

        when(customDriverRepository.findDriverNearLocation(any(Point.class), anyDouble())).thenReturn(driver);
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        // When
        rideRequestService.findNearestDriver(1, passengerLocation);

        // Then
        verify(customDriverRepository, times(1)).findDriverNearLocation(any(Point.class), anyDouble());
        verify(rideRepository, times(1)).save(any(Ride.class));
    }

    @Test
    void testFindNearestDriverWhenNoDriverFound() {
        // Given
        Location passengerLocation = new Location(32.7777, -0.2260);
        when(customDriverRepository.findDriverNearLocation(any(), anyDouble())).thenReturn(null);

        // When & Then
        assertThrows(RuntimeException.class, () -> rideRequestService.findNearestDriver(1, passengerLocation));

        // Verify
        verify(customDriverRepository, times(1)).findDriverNearLocation(any(Point.class), anyDouble());
        verify(customDriverRepository, never()).save(any(Driver.class), anyString());
    }
}