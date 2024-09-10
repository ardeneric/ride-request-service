package com.challenge.ride;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RideApplication {

	public static void main(String[] args) {
		SpringApplication.run(RideApplication.class, args);
	}

}
