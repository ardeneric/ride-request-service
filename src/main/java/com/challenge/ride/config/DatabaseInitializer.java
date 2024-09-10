package com.challenge.ride.config;

import com.challenge.ride.entity.Driver;
import com.challenge.ride.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.geo.Point;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer {

    private final DriverRepository driverRepository;
    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            driverRepository.deleteAll();

            log.info("==== Prepopulating DB with drivers =====");
            driverRepository.saveAll(Arrays.asList(
                new Driver(null, new Point(-0.2260, 32.7777), true),
                new Driver(null, new Point(-0.1279, 55.4212), true),
                new Driver(null, new Point(-0.2571, 40.5559), false),
                new Driver(null, new Point(-0.1278, 57.5064), true)
            ));
        };
    }
}
