package com.casestudy.vehicleaggregator.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationResponse {
    private int id;
    private LocationDto location;

}
