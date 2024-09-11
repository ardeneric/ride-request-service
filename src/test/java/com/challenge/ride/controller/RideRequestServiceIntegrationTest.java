package com.challenge.ride.controller;

import com.challenge.ride.entity.Driver;
import com.challenge.ride.model.Location;
import com.challenge.ride.model.RideRequest;
import com.challenge.ride.service.RideRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Slf4j
class RideRequestServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
    void testSubmitRideRequest() throws Exception {
        final RideRequest rideRequest = RideRequest.builder().rideId(1).passengerLocation(new Location(32.7777, -0.2260)).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(rideRequest)))
                .andExpect(status().isAccepted());
    }

    @Test
    void testGetNearestDriver_Success() throws Exception {
        Driver mockDriver = new Driver();
        mockDriver.setAvailable(true);
        Mockito.when(rideRequestService.findNearestDriver(1)).thenReturn(Optional.of(mockDriver));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rides/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(true));
    }

    @Test
    void testGetNearestDriver_NotFound() throws Exception {
        Mockito.when(rideRequestService.findNearestDriver(1)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rides/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}