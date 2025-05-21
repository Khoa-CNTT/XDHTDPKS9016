package com.tourism.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BookingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingBackendApplication.class, args);
    }

}
