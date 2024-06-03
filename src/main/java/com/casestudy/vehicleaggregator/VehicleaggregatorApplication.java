package com.casestudy.vehicleaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("com.casestudy.vehicleaggregator.config")
@SpringBootApplication
public class VehicleaggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleaggregatorApplication.class, args);
    }

}
