package com.casestudy.vehicleaggregator.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusFallbackFareConfigDto {
    private String baseUrl;
    private String busesPath;
}
