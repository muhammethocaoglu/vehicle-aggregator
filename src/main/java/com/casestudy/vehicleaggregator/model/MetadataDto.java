package com.casestudy.vehicleaggregator.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MetadataDto {
    private Integer id;
    private String licensePlate;
}
