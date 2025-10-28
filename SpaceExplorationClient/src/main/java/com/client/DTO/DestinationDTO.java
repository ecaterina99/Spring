package com.client.DTO;

import com.client.enums.DestinationEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationDTO {
    private Integer id;
    private String destinationName;
    private String distanceFromEarth;
    private String description;
    private Integer gravity;
    private List<MissionDTO> missions = new ArrayList<>();

    private DestinationEnums.EntityType entityType;
}
