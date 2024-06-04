package com.casestudy.vehicleaggregator.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AggregateBusFareResponse {
    private Integer id;
    private String licensePlate;
    private Integer fare;
}
