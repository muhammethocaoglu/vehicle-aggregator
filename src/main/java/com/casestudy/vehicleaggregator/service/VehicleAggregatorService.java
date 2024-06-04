package com.casestudy.vehicleaggregator.service;

import com.casestudy.vehicleaggregator.config.ConfigProperties;
import com.casestudy.vehicleaggregator.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleAggregatorService {

    private final ConfigProperties configProperties;
    private final WebClient busLiveFareAndLocationWebClient;
    private final WebClient trainLocationWebClient;
    private final WebClient busFallbackFareWebClient;
    private final WebClient busMetadataWebClient;

    public Mono<List<AggregateBusFareResponse>> retrieveBusFares() {
        Flux<MetadataDto> metadataDtoFlux = retrieveMetadataDtoFlux();

        return metadataDtoFlux.flatMapSequential(metadataDto -> {
                    Mono<BusFareResponse> busFareResponseMono = retrieveBusLiveFare(metadataDto);
                    return busFareResponseMono
                            .map(busFareResponse -> populateAggregateBusFareResponse(metadataDto, busFareResponse))
                            .onErrorResume(throwable -> {
                                log.warn("Bus live fare and location service returns error: {}. Using fallback fares",
                                        throwable.getMessage());
                                Mono<BusFareResponse> busFallbackFareResponseMono = retrieveBusFallbackFare(metadataDto);
                                return busFallbackFareResponseMono
                                        .map(busFallbackFareResponse ->
                                                populateAggregateBusFareResponse(metadataDto, busFallbackFareResponse));
                            });
                }
        ).collectList();
    }

    public Flux<EnrichedLocationResponse> retrieveLocationStream() {
        Flux<LocationResponse> busLocationStream = retrieveBusLocationStream();
        Flux<LocationResponse> trainLocationStream = retrieveTrainLocationStream();

        Flux<EnrichedLocationResponse> enrichedBusLocationStream = busLocationStream
                .map(locationResponse -> EnrichedLocationResponse.builder()
                        .id(locationResponse.getId())
                        .location(locationResponse.getLocation())
                        .type(VehicleType.BUS)
                        .build());
        Flux<EnrichedLocationResponse> enrichedTrainLocationStream = trainLocationStream
                .map(locationResponse -> EnrichedLocationResponse.builder()
                        .id(locationResponse.getId())
                        .location(locationResponse.getLocation())
                        .type(VehicleType.TRAIN)
                        .build());

        return Flux.merge(enrichedBusLocationStream, enrichedTrainLocationStream);
    }

    private Flux<LocationResponse> retrieveTrainLocationStream() {
        return trainLocationWebClient
                .get()
                .uri(configProperties.getTrainLiveLocationService().getLocationStreamPath())
                .retrieve()
                .bodyToFlux(LocationResponse.class);
    }

    private Flux<LocationResponse> retrieveBusLocationStream() {
        return busLiveFareAndLocationWebClient
                .get()
                .uri(configProperties.getBusLiveFareAndLocationService().getLocationStreamPath())
                .retrieve()
                .bodyToFlux(LocationResponse.class)
                .retryWhen(Retry.maxInARow(3));
    }

    private Mono<BusFareResponse> retrieveBusFallbackFare(MetadataDto metadataDto) {
        return busFallbackFareWebClient
                .get()
                .uri(configProperties.getBusFallbackFareService().getBusesPath()
                        + String.format("/%d/fare", metadataDto.getId()))
                .retrieve()
                .bodyToMono(BusFareResponse.class);
    }

    private static AggregateBusFareResponse populateAggregateBusFareResponse(MetadataDto metadataDto,
                                                                             BusFareResponse busFareResponse) {
        return AggregateBusFareResponse
                .builder()
                .id(metadataDto.getId())
                .licensePlate(metadataDto.getLicensePlate())
                .fare(busFareResponse.getFare())
                .build();
    }

    private Mono<BusFareResponse> retrieveBusLiveFare(MetadataDto metadataDto) {
        return busLiveFareAndLocationWebClient
                .get()
                .uri(configProperties.getBusLiveFareAndLocationService().getBusesPath()
                        + String.format("/%d/fare", metadataDto.getId()))
                .retrieve()
                .bodyToMono(BusFareResponse.class);
    }

    private Flux<MetadataDto> retrieveMetadataDtoFlux() {
        return busMetadataWebClient.get()
                .uri(configProperties.getBusMetadataService().getMetadataContextPath())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(MetadataDto.class);
    }

}
