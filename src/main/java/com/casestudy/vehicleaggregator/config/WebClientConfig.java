package com.casestudy.vehicleaggregator.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final ConfigProperties configProperties;

    @Bean
    public WebClient busLiveFareAndLocationWebClient() {
        return WebClient.builder()
                .baseUrl(configProperties.getBusLiveFareAndLocationService().getBaseUrl())
                .build();
    }

    @Bean
    public WebClient trainLocationWebClient() {
        return WebClient.builder()
                .baseUrl(configProperties.getTrainLiveLocationService().getBaseUrl())
                .build();
    }
}
