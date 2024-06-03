package com.casestudy.vehicleaggregator.controller;

import com.casestudy.vehicleaggregator.config.ConfigProperties;
import com.casestudy.vehicleaggregator.model.EnrichedLocationResponse;
import com.casestudy.vehicleaggregator.model.LocationResponse;
import com.casestudy.vehicleaggregator.model.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class VehicleAggregatorController {
    private final ConfigProperties configProperties;
    private final WebClient busLiveFareAndLocationWebClient;
    private final WebClient trainLocationWebClient;

    @GetMapping(path = "/vehicle-location-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EnrichedLocationResponse> retrieve() {
        Flux<LocationResponse> busLocationStream =  busLiveFareAndLocationWebClient
                .get()
                .uri(configProperties.getBusLiveFareAndLocationService().getLocationStreamPath())
                .retrieve()
                .bodyToFlux(LocationResponse.class)
                .retryWhen(Retry.maxInARow(3));
        Flux<LocationResponse> trainLocationStream =  trainLocationWebClient
                .get()
                .uri(configProperties.getTrainLiveLocationService().getLocationStreamPath())
                .retrieve()
                .bodyToFlux(LocationResponse.class);

        Flux<EnrichedLocationResponse> enrichedBusLocationStream = busLocationStream.map(locationResponse -> EnrichedLocationResponse.builder()
                .id(locationResponse.getId())
                .location(locationResponse.getLocation())
                .type(VehicleType.BUS)
                .build());
        Flux<EnrichedLocationResponse> enrichedTrainLocationStream = trainLocationStream.map(locationResponse -> EnrichedLocationResponse.builder()
                .id(locationResponse.getId())
                .location(locationResponse.getLocation())
                .type(VehicleType.TRAIN)
                .build());

        return Flux.merge(enrichedBusLocationStream, enrichedTrainLocationStream);
    }

}