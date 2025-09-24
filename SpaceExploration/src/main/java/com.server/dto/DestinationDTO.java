package com.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.models.Destination;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Space destination information")
public class DestinationDTO {
    private int id;
    @NotBlank(message = "Destination name is required")
    @Size(min = 2, max = 100, message = "Destination name must be between 2 and 100 characters")
    private String destinationName;
    @NotBlank(message = "Distance from earth is required")
    @Size(min = 2, max = 50, message = "Destination name must be between 2 and 50 characters")
    private String distanceFromEarth;
    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 500, message = "Destination name must be between 2 and 500 characters")
    private String description;
    private Integer gravity;
    private String image;
    @NotNull(message = "Entity type is required")
    private Destination.EntityType entityType;

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = "Space destination with missions information")
    public static class DestinationWithMissionsDTO {
        private int id;
        private String destinationName;
        private String distanceFromEarth;
        private String description;
        private Integer gravity;
        private String image;
        private Destination.EntityType entityType;

        @Schema(description = "List of missions for this destination")
        private List<MissionDTO> missions;


        public static DestinationWithMissionsDTO DestinationWithMissions(Destination destination) {
            return DestinationWithMissionsDTO.builder()
                    .id(destination.getId())
                    .destinationName(destination.getDestinationName())
                    .distanceFromEarth(destination.getDistanceFromEarth())
                    .description(destination.getDescription())
                    .gravity(destination.getGravity())
                    .image(destination.getImage())
                    .entityType(destination.getEntityType())
                    .missions(destination.getMissions().stream()
                            .map(mission -> MissionDTO.builder()
                                    .id(mission.getId())
                                    .name(mission.getName())
                                    .build())
                            .toList())
                    .build();
        }
    }
}


