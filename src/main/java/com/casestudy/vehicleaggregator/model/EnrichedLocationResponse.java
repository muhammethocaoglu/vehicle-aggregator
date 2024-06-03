package com.casestudy.vehicleaggregator.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrichedLocationResponse {
    private int id;
    private VehicleType type;
    private LocationDto location;
}
