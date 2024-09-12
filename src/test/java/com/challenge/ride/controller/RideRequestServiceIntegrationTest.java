package com.challenge.ride.controller;

import com.challenge.ride.entity.Driver;
import com.challenge.ride.model.Location;
import com.challenge.ride.model.RideRequest;
import com.challenge.ride.service.RideRequestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Slf4j
class RideRequestServiceIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RideRequestService rideRequestService;

    // Define MongoDB test container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:7.0")).withExposedPorts(27017);

    // Define RabbitMQ test container
    private static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13.7")).withExposedPorts(5672, 15672);

    @BeforeAll
    static void setUp() {
        // Start MongoDB container
        mongoDBContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());

        // Start RabbitMQ container
        rabbitMQContainer.start();
        System.setProperty("spring.rabbitmq.host", rabbitMQContainer.getHost());
        System.setProperty("spring.rabbitmq.port", rabbitMQContainer.getMappedPort(5672).toString());
        System.setProperty("spring.rabbitmq.username", rabbitMQContainer.getAdminUsername());
        System.setProperty("spring.rabbitmq.password", rabbitMQContainer.getAdminPassword());

    }

    @Test
    void testSubmitRideRequest() {
        final RideRequest rideRequest = RideRequest.builder().riderId(1).passengerLocation(new Location(32.7777, -0.2260)).build();
        webTestClient.post().uri("/api/rides")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(rideRequest), RideRequest.class)
                .exchange()
                .expectStatus().isAccepted();
    }

    @Test
    void testGetNearestDriver_Success() {
        Driver mockDriver = new Driver();
        mockDriver.setAvailable(true);
        Mockito.when(rideRequestService.findNearestDriver(1)).thenReturn(Optional.of(mockDriver));

        webTestClient.get().uri("/api/rides/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.available").isEqualTo(true);
    }

    @Test
    void testGetNearestDriver_NotFound() {
        Mockito.when(rideRequestService.findNearestDriver(1)).thenReturn(Optional.empty());

        webTestClient.get().uri("/api/rides/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}