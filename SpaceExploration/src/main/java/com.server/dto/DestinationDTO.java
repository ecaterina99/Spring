package com.server.dto;

import com.server.models.Destination;
import com.server.models.Mission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(description = "Space destination information")

public class DestinationDTO {
    private int id;
    private String destinationName;
    private String distanceFromEarth;
    private String description;
    private Integer gravity;
    private String image;
    private Destination.EntityType entityType;
    private List<Mission> missions;
}
