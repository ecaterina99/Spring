package com.client.DTO;

import com.client.enums.DestinationEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationDTO {
    private int id;
    private String destinationName;
    private String distanceFromEarth;
    private String description;
    private Integer gravity;
    private String image;
    private DestinationEnums.EntityType entityType;
}
