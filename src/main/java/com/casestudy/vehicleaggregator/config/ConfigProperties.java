package com.casestudy.vehicleaggregator.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "client")
@ConfigurationPropertiesScan
@Configuration
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigProperties {
    private BusLiveFareAndLocationConfigDto busLiveFareAndLocationService;
    private LocationStreamClientConfigDto trainLiveLocationService;
    private BusFallbackFareConfigDto busFallbackFareService;
    private BusMetadataConfigDto busMetadataService;
}
