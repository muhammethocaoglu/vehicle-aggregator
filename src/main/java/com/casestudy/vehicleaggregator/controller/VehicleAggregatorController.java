package com.casestudy.vehicleaggregator.controller;

import com.casestudy.vehicleaggregator.model.AggregateBusFareResponse;
import com.casestudy.vehicleaggregator.model.EnrichedLocationResponse;
import com.casestudy.vehicleaggregator.service.VehicleAggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class VehicleAggregatorController {

    private final VehicleAggregatorService vehicleAggregatorService;

    @GetMapping(path = "/vehicle-location-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EnrichedLocationResponse> retrieve() {
        return vehicleAggregatorService.retrieveLocationStream();
    }

    @GetMapping(path = "/buses", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<AggregateBusFareResponse>> retrieveBusFares() {
        return vehicleAggregatorService.retrieveBusFares();
    }

}