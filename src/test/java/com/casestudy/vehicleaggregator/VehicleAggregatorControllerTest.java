package com.casestudy.vehicleaggregator;

import com.casestudy.vehicleaggregator.config.ConfigProperties;
import com.casestudy.vehicleaggregator.config.WebClientConfig;
import com.casestudy.vehicleaggregator.controller.VehicleAggregatorController;
import com.casestudy.vehicleaggregator.model.AggregateBusFareResponse;
import com.casestudy.vehicleaggregator.model.EnrichedLocationResponse;
import com.casestudy.vehicleaggregator.model.VehicleType;
import com.casestudy.vehicleaggregator.service.VehicleAggregatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

@EnableConfigurationProperties(value = ConfigProperties.class)
@TestPropertySource("classpath:application-test.properties")
@WebFluxTest(controllers = VehicleAggregatorController.class)
@ContextConfiguration(classes = {
        VehicleAggregatorController.class,
        VehicleAggregatorService.class,
        WebClientConfig.class
})
public class VehicleAggregatorControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void test_should_return_location_stream_when_retrieve() {
        Flux<EnrichedLocationResponse> result = webClient
                .get()
                .uri("/vehicle-location-stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(EnrichedLocationResponse.class)
                .getResponseBody();


        StepVerifier.create(result)
                .expectNextMatches(enrichedLocationResponse ->
                        List.of(VehicleType.BUS, VehicleType.TRAIN).contains(enrichedLocationResponse.getType()))
                .expectNextMatches(enrichedLocationResponse ->
                        List.of(VehicleType.BUS, VehicleType.TRAIN).contains(enrichedLocationResponse.getType()))
                .thenCancel()
                .verify();

    }

    @Test
    void test_should_return_bus_fare_list_when_retrieve_bus_fare() {
        Flux<AggregateBusFareResponse> aggregateBusFareFlux = webClient
                .get()
                .uri("/buses")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(AggregateBusFareResponse.class)
                .getResponseBody();

        StepVerifier.create(aggregateBusFareFlux).expectNextCount(9).verifyComplete();

    }
}
