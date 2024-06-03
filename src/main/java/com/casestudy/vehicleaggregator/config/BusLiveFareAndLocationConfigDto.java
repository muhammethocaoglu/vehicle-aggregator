package com.casestudy.vehicleaggregator.config;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusLiveFareAndLocationConfigDto extends LocationStreamClientConfigDto {
    private String busesPath;
}
