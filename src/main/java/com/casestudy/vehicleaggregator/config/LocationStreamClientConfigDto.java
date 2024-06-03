package com.casestudy.vehicleaggregator.config;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationStreamClientConfigDto {
    private String baseUrl;
    private String locationStreamPath;
}
